package app;

import clases.EstadoProducto;
import clases.Smartphone;
import clases.Laptop;
import clases.CategoriaProducto;
import clases.Accesorio;
import clases.Producto;
import excepciones.DatoInvalidoException;
import excepciones.EntidadNoEncontradaException;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


import persistencia.ExportadorTxt;
import persistencia.SerializadorJSON;
import persistencia.SerializadorCSV;

import gestion.GestorEntidades;

import comparadores.ComparadorPrecioAsc;
import comparadores.ComparadorStockDesc;
import java.util.Optional;
import java.nio.file.Path;
import java.util.List;

public class MainApp extends Application {

    private GestorEntidades<Producto> gestor = new GestorEntidades<>();
    private ObservableList<Producto> datosTabla = FXCollections.observableArrayList();

    private TableView<Producto> tabla = new TableView<>();

    // inputs
    private TextField txtId = new TextField();
    private TextField txtNombre = new TextField();
    private TextField txtPrecio = new TextField();
    private TextField txtStock = new TextField();
    private TextField txtRam = new TextField();
    private TextField txtAlmacenamiento = new TextField();
    private TextField txtSistemaOperativo = new TextField();
    private TextField txtTipoAccesorio = new TextField();
    private TextField txtColorAccesorio = new TextField();
    private ComboBox<CategoriaProducto> cmbCategoria = new ComboBox<>();
    private ComboBox<EstadoProducto> cmbEstado = new ComboBox<>();

    private TextField txtFiltroNombre = new TextField();

    @Override
    public void start(Stage primaryStage) {
        configurarTabla();
        configurarControles();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        root.setCenter(tabla);
        root.setTop(crearZonaFiltros());
        root.setBottom(crearZonaFormulario());
        primaryStage.setTitle("CRUD - Productos - Ezequiel Do Campo");
        Image icono = new Image("/icono.png");
        primaryStage.getIcons().add(icono);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void configurarTabla() {
        // columnas        
        TableColumn<Producto, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));

        TableColumn<Producto, Number> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrecio()));

        TableColumn<Producto, Number> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()));

        TableColumn<Producto, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategoria().name()));

        TableColumn<Producto, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado().name()));

        // RAM (solo laptop)
        TableColumn<Producto, String> colRam = new TableColumn<>("RAM (GB)");
        colRam.setCellValueFactory(c -> {
            Producto p = c.getValue();
            if (p instanceof Laptop) {
                return new SimpleStringProperty(String.valueOf(((Laptop) p).getRamGb()));
            }
            return new SimpleStringProperty("-");
        });

        // almacenamiento (laptop o smartphone)
        TableColumn<Producto, String> colAlm = new TableColumn<>("Alm. (GB)");
        colAlm.setCellValueFactory(c -> {
            Producto p = c.getValue();
            if (p instanceof Laptop) {
                return new SimpleStringProperty(String.valueOf(((Laptop) p).getAlmacenamientoGb()));
            } else if (p instanceof Smartphone) {
                return new SimpleStringProperty(String.valueOf(((Smartphone) p).getAlmacenamientoGb()));
            }
            return new SimpleStringProperty("-");
        });

        // SO (solo smartphone)
        TableColumn<Producto, String> colSO = new TableColumn<>("SO");
        colSO.setCellValueFactory(c -> {
            Producto p = c.getValue();
            if (p instanceof Smartphone) {
                return new SimpleStringProperty(((Smartphone) p).getSistemaOperativo());
            }
            return new SimpleStringProperty("-");
        });

        // tipo (solo accesorio)
        TableColumn<Producto, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(c -> {
            Producto p = c.getValue();
            if (p instanceof Accesorio) {
                return new SimpleStringProperty(((Accesorio) p).getTipo());
            }
            return new SimpleStringProperty("-");
        });

        // Color (solo Accesorio)
        TableColumn<Producto, String> colColor = new TableColumn<>("Color");
        colColor.setCellValueFactory(c -> {
            Producto p = c.getValue();
            if (p instanceof Accesorio) {
                return new SimpleStringProperty(((Accesorio) p).getColor());
            }
            return new SimpleStringProperty("-");
        });

        tabla.getColumns().setAll(
                colId,
                colNombre,
                colPrecio,
                colStock,
                colCategoria,
                colEstado,
                colRam,
                colAlm,
                colSO,
                colTipo,
                colColor
        );
        tabla.setItems(datosTabla);
    }


    private void configurarControles() {
        cmbCategoria.setItems(FXCollections.observableArrayList(CategoriaProducto.values()));
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoProducto.values()));
        cmbCategoria.getSelectionModel().select(CategoriaProducto.ACCESORIO);
        cmbEstado.getSelectionModel().select(EstadoProducto.NUEVO);
    }

    private HBox crearZonaFiltros() {
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(5));

        Button btnFiltrarNombre = new Button("Filtrar por nombre");
        btnFiltrarNombre.setOnAction(e -> filtrarPorNombre());

        Button btnOrdenarNombre = new Button("Ordenar por nombre (natural)");
        btnOrdenarNombre.setOnAction(e -> ordenarNatural());

        Button btnOrdenarPrecio = new Button("Ordenar por precio");
        btnOrdenarPrecio.setOnAction(e -> ordenarPorPrecio());

        Button btnOrdenarStock = new Button("Ordenar por stock");
        btnOrdenarStock.setOnAction(e -> ordenarPorStock());
        
        Button btnAgregarStock = new Button("Agregar 20 de stock");
        btnAgregarStock.setOnAction(e -> agregarStock20());

        Button btnGuardarJson = new Button("Guardar JSON");
        btnGuardarJson.setOnAction(e -> guardarJSON());

        Button btnCargarJson = new Button("Cargar JSON");
        btnCargarJson.setOnAction(e -> cargarJSON());
        
        Button btnGuardarCsv = new Button("Guardar CSV");
        btnGuardarCsv.setOnAction(e -> guardarCSV());

        Button btnCargarCsv = new Button("Cargar CSV");
        btnCargarCsv.setOnAction(e -> cargarCSV());

        Button btnExportarTxt = new Button("Exportar TXT");
        btnExportarTxt.setOnAction(e -> exportarTxt());

        hbox.getChildren().addAll(
                new Label("Nombre:"),
                txtFiltroNombre,
                btnFiltrarNombre,
                btnOrdenarNombre,
                btnOrdenarPrecio,
                btnOrdenarStock,
                btnAgregarStock,
                btnGuardarJson,
                btnCargarJson,
                btnGuardarCsv,
                btnCargarCsv,
                btnExportarTxt
        );
        return hbox;
    }

    private Integer parseOptionalInt(String text) throws NumberFormatException {
        if (text == null || text.isBlank()) {
            return null;
        }
        return Integer.parseInt(text);
    }
    
    private GridPane crearZonaFormulario() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(5);

        int row = 0;
        grid.add(new Label("ID:"), 0, row);
        grid.add(txtId, 1, row);

        row++;
        grid.add(new Label("Nombre:"), 0, row);
        grid.add(txtNombre, 1, row);

        row++;
        grid.add(new Label("Precio:"), 0, row);
        grid.add(txtPrecio, 1, row);

        row++;
        grid.add(new Label("Stock:"), 0, row);
        grid.add(txtStock, 1, row);

        row++;
        grid.add(new Label("Categoría:"), 0, row);
        grid.add(cmbCategoria, 1, row);

        row++;
        grid.add(new Label("Estado:"), 0, row);
        grid.add(cmbEstado, 1, row);
        
        row++;
        grid.add(new Label("RAM (GB) [Laptop]:"), 0, row);
        grid.add(txtRam, 1, row);

        row++;
        grid.add(new Label("Almacenamiento (GB) [Laptop/Smart]:"), 0, row);
        grid.add(txtAlmacenamiento, 1, row);

        row++;
        grid.add(new Label("SO [Smartphone]:"), 0, row);
        grid.add(txtSistemaOperativo, 1, row);

        row++;
        grid.add(new Label("Tipo [Accesorio]:"), 0, row);
        grid.add(txtTipoAccesorio, 1, row);

        row++;
        grid.add(new Label("Color [Accesorio]:"), 0, row);
        grid.add(txtColorAccesorio, 1, row);

        row++;
        HBox botones = new HBox(10);
        Button btnAgregar = new Button("Agregar");
        btnAgregar.setOnAction(e -> agregarProducto());

        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setOnAction(e -> actualizarProducto());

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> eliminarProducto());

        botones.getChildren().addAll(btnAgregar, btnActualizar, btnEliminar);
        grid.add(botones, 0, row, 2, 1);

        row++;
        Text nombre = new Text("Ezequiel Do Campo");
        grid.add(nombre, 0, row);

        return grid;
    }

    // botones

    private int generarNuevoId() {
        return gestor.listar().stream()
                .mapToInt(Producto::getId)
                .max()
                .orElse(0) + 1;
    }

    private void agregarProducto() {
        try {
            Producto nuevo = construirProductoParaAgregar();
            gestor.agregar(nuevo);
            refrescarTabla();
            limpiarFormulario();
        } catch (Exception ex) {
            mostrarError("Error al agregar: " + ex.getMessage());
        }
    }
    
    private Producto construirProductoParaAgregar() throws NumberFormatException {
        int id;
        if (txtId.getText() == null || txtId.getText().isBlank()) {
            id = generarNuevoId();
        } else {
            id = Integer.parseInt(txtId.getText());
        }

        String nombre = txtNombre.getText();
        double precio = Double.parseDouble(txtPrecio.getText());
        int stock = Integer.parseInt(txtStock.getText());
        CategoriaProducto categoria = cmbCategoria.getValue();
        EstadoProducto estado = cmbEstado.getValue();

        // campos opcionales
        Integer ram = null;
        Integer almacenamiento = null;
        String so = null;
        String tipoAcc = null;
        String colorAcc = null;

        try {
            ram = parseOptionalInt(txtRam.getText());
        } catch (NumberFormatException e) {
            mostrarError("RAM debe ser un numero entero");
            throw e;
        }

        try {
            almacenamiento = parseOptionalInt(txtAlmacenamiento.getText());
        } catch (NumberFormatException e) {
            mostrarError("Almacenamiento debe ser un numero entero");
            throw e;
        }

        if (txtSistemaOperativo.getText() != null && !txtSistemaOperativo.getText().isBlank()) {
            so = txtSistemaOperativo.getText();
        }

        if (txtTipoAccesorio.getText() != null && !txtTipoAccesorio.getText().isBlank()) {
            tipoAcc = txtTipoAccesorio.getText();
        }

        if (txtColorAccesorio.getText() != null && !txtColorAccesorio.getText().isBlank()) {
            colorAcc = txtColorAccesorio.getText();
        }

        // subclase depende de categoria
        if (categoria == CategoriaProducto.LAPTOP) {
            // si ram y almacenamiento esta lleno usa constructor completo
            if (ram != null && almacenamiento != null) {
                return new Laptop(id, nombre, precio, stock, categoria, estado, ram, almacenamiento);
            } else {
                // si no usa la sobrecarga generica
                return new Laptop(id, nombre, precio, stock, categoria, estado);
            }
        } else if (categoria == CategoriaProducto.SMARTPHONE) {
            if (so != null && almacenamiento != null) {
                return new Smartphone(id, nombre, precio, stock, categoria, estado, so, almacenamiento);
            } else {
                return new Smartphone(id, nombre, precio, stock, categoria, estado);
            }
        } else {
            if (tipoAcc != null && colorAcc != null) {
                return new Accesorio(id, nombre, precio, stock, categoria, estado, tipoAcc, colorAcc);
            } else {
                return new Accesorio(id, nombre, precio, stock, categoria, estado);
            }
        }
    }


    private void actualizarProducto() {
        try {
            if (txtId.getText() == null || txtId.getText().isBlank()) {
                mostrarError("Para actualizar indica el ID");
                return;
            }

            int id = Integer.parseInt(txtId.getText());

            var opt = gestor.buscarPorId(id);
            if (opt.isEmpty()) {
                mostrarError("No se encontro producto con ID " + id);
                return;
            }

            Producto p = opt.get();

            if (txtNombre.getText() != null && !txtNombre.getText().isBlank()) {
                p.setNombre(txtNombre.getText());
            }

            if (txtPrecio.getText() != null && !txtPrecio.getText().isBlank()) {
                p.setPrecio(Double.parseDouble(txtPrecio.getText()));
            }

            if (txtStock.getText() != null && !txtStock.getText().isBlank()) {
                p.setStock(Integer.parseInt(txtStock.getText()));
            }

            if (cmbCategoria.getValue() != null) {
                p.setCategoria(cmbCategoria.getValue());
            }

            if (cmbEstado.getValue() != null) {
                p.setEstado(cmbEstado.getValue());
            }

            // campos extra opcionales
            if (p instanceof Laptop) {
                Laptop lap = (Laptop) p;
                if (txtRam.getText() != null && !txtRam.getText().isBlank()) {
                    lap.setRamGb(Integer.parseInt(txtRam.getText()));
                }
                if (txtAlmacenamiento.getText() != null && !txtAlmacenamiento.getText().isBlank()) {
                    lap.setAlmacenamientoGb(Integer.parseInt(txtAlmacenamiento.getText()));
                }
            } else if (p instanceof Smartphone) {
                Smartphone sp = (Smartphone) p;
                if (txtSistemaOperativo.getText() != null && !txtSistemaOperativo.getText().isBlank()) {
                    sp.setSistemaOperativo(txtSistemaOperativo.getText());
                }
                if (txtAlmacenamiento.getText() != null && !txtAlmacenamiento.getText().isBlank()) {
                    sp.setAlmacenamientoGb(Integer.parseInt(txtAlmacenamiento.getText()));
                }
            } else if (p instanceof Accesorio) {
                Accesorio ac = (Accesorio) p;
                if (txtTipoAccesorio.getText() != null && !txtTipoAccesorio.getText().isBlank()) {
                    ac.setTipo(txtTipoAccesorio.getText());
                }
                if (txtColorAccesorio.getText() != null && !txtColorAccesorio.getText().isBlank()) {
                    ac.setColor(txtColorAccesorio.getText());
                }
            }

            refrescarTabla();
            limpiarFormulario();

        } catch (NumberFormatException nfe) {
            mostrarError("Dato invalido ID, precio, stock, RAM o almacenamiento");
        }
    }

    private void eliminarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText());
            gestor.eliminar(id);
            refrescarTabla();
            limpiarFormulario();
        } catch (EntidadNoEncontradaException ex) {
            mostrarError(ex.getMessage());
        } catch (NumberFormatException nfe) {
            mostrarError("ID invalido");
        }
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        txtRam.clear();
        txtAlmacenamiento.clear();
        txtSistemaOperativo.clear();
        txtTipoAccesorio.clear();
        txtColorAccesorio.clear();
    }
    
    private void filtrarPorNombre() {
        String texto = txtFiltroNombre.getText();
        if (texto == null || texto.isBlank()) {
            refrescarTabla();
            return;
        }
        List<Producto> filtrados = gestor.filtrarPorNombre(texto);
        datosTabla.setAll(filtrados);
    }

    private void ordenarNatural() {
        List<Producto> lista = gestor.listar();
        lista.sort(null);
        datosTabla.setAll(lista);
    }

    private void ordenarPorPrecio() {
        List<Producto> lista = gestor.listar();
        lista.sort(new ComparadorPrecioAsc());
        datosTabla.setAll(lista);
    }

    private void ordenarPorStock() {
        List<Producto> lista = gestor.listar();
        lista.sort(new ComparadorStockDesc());
        datosTabla.setAll(lista);
    }

    private void agregarStock20() {
        gestor.aplicarAccion(p -> p.setStock(p.getStock() + 20));
        refrescarTabla();
    }

    private void guardarJSON() {
        // modal de confirmación
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar guardado");
        confirm.setHeaderText("Estas seguro que querés guardar en JSON?");
        confirm.setContentText("Esto sobrescribira el archivo productos.json si ya existe");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            Path ruta = Path.of("productos.json");
            SerializadorJSON serializador = new SerializadorJSON();
            serializador.guardar(gestor.listar(), ruta);

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Guardado exitoso");
            ok.setHeaderText(null);
            ok.setContentText("El archivo productos.json se guardó correctamente.");
            ok.showAndWait();

        } catch (Exception e) {
            mostrarError("Error al guardar JSON: " + e.getMessage());
        }
    }

    private void cargarJSON() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar carga JSON");
        confirm.setHeaderText("Cargar datos desde productos.json?");
        confirm.setContentText("Esto agregara los productos del archivo a la lista actual");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            Path ruta = Path.of("productos.json");
            SerializadorJSON serializador = new SerializadorJSON();
            List<Producto> lista = serializador.cargar(ruta);

            int agregados = 0;
            for (Producto p : lista) {
                try {
                    gestor.agregar(p);
                    agregados++;
                } catch (DatoInvalidoException ex) {
                }
            }

            refrescarTabla();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Carga JSON");
            ok.setHeaderText(null);
            ok.setContentText("Se cargaron " + agregados + " productos desde productos.json.");
            ok.showAndWait();

        } catch (Exception e) {
            mostrarError("Error al cargar JSON: " + e.getMessage());
        }
    }


    private void guardarCSV() {
    // modal de confirmación
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle("Confirmar guardado CSV");
    confirm.setHeaderText("Guardar archivo CSV?");
    confirm.setContentText("Esto sobrescribira el archivo productos.csv si ya existe");

    Optional<ButtonType> result = confirm.showAndWait();
    if (result.isEmpty() || result.get() != ButtonType.OK) {
        return;
    }

        try {
            Path ruta = Path.of("productos.csv");
            SerializadorCSV.guardar(gestor.listar(), ruta);

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Guardado CSV");
            ok.setHeaderText(null);
            ok.setContentText("El archivo productos.csv fue guardado correctamente");
            ok.showAndWait();

        } catch (Exception e) {
            mostrarError("Error al guardar CSV: " + e.getMessage());
        }
    }

   private void cargarCSV() {
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle("Confirmar carga CSV");
    confirm.setHeaderText("Cargar datos desde productos.csv?");
    confirm.setContentText("Esto agregara los productos del archivo a la lista actual");

    Optional<ButtonType> result = confirm.showAndWait();
    if (result.isEmpty() || result.get() != ButtonType.OK) {
        return;
    }

        try {
            Path ruta = Path.of("productos.csv");
            List<Producto> lista = SerializadorCSV.cargar(ruta);

            for (Producto p : lista) {
                try {
                    gestor.agregar(p);
                } catch (DatoInvalidoException ex) {
                }
            }

            refrescarTabla();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Carga CSV");
            ok.setHeaderText(null);
            ok.setContentText("Se cargaron " + lista.size() + " productos desde productos.csv");
            ok.showAndWait();

        } catch (Exception e) {
            mostrarError("Error al cargar CSV: " + e.getMessage());
        }
    }
   
    private void exportarTxt() {
        try {
            String fecha = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            Path ruta = Path.of("textos/productostexto" + fecha + ".txt");
            ExportadorTxt.exportarListaFiltrada(datosTabla, ruta, "Productos mostrados en pantalla");
     
            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Exportado correctamente");
            ok.setHeaderText(null);
            ok.setContentText("El archivo TXT se exporto correctamente:\n\n" + ruta.getFileName());
            ok.showAndWait();

        } catch (Exception e) {
            mostrarError("Error al exportar TXT: " + e.getMessage());
        }
    }

    private void refrescarTabla() {
        datosTabla.setAll(gestor.listar());
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
