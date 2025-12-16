# CRUD - Productos

## Sobre mi
Soy Ezequiel Do Campo, estudiante de la Universidad Tecnológica Nacional, pasando al cuarto cuatrimestre de la carrera después de estas vacaciones. Ya trabajé en proyectos con HTML, CSS y JavaScript, por lo que tenia algo de experiencia en la creación de un programa grafico, pero esta es mi primera vez usando JavaFX y lo aprendí por mi parte

## Resumen del proyecto
La aplicación de gestión de productos permite al usuario crear, leer, actualizar y leer (CRUD) una lista de productos en forma de tabla, guardar esos productos tanto en archivos .csv como en archivos .json, y luego volver a cargarlos. Tambien puede exportar productos a formato texto y que la tabla sea legible para el usuario de esa manera.
Cada producto puede ser una de tres categorias: Laptop, Smartphone o Accesorio, cada uno de estos productos es una clase y heredan de la clase Producto. Cada categoría tiene 2 atributos mas que el constructor de la clase abstracta Producto, relevantes a la categoría.

## Funcionalidades principales
- Creación de productos
- Actualización de productos
- Borrado de productos
- Listado personalizado de productos por distintos ordenamientos (nombre, precio o stock)
- Filtrado por input del usuario
- Agregado masivo de stock
- Persistencia con exportación e importación a CSV y JSON
- Guardado de la lista a TXT legible

Capturas de pantalla de la aplicación:

<img width="1399" height="887" alt="Screenshot 2025-12-16 183229" src="https://github.com/user-attachments/assets/f19aa417-f0bf-4871-924a-fa46a2926499" />

## Diagrama de clases UML

<img width="1590" height="790" alt="Diagram 2025-12-10 21-55-34" src="https://github.com/user-attachments/assets/5773c587-7453-4be0-8694-b778d4aafbe6" />

## Archivos generados por la aplicación

### Archivo JSON
Nombre: productos.json
Contiene todos los productos con sus atributos

Ejemplo:

{
"sistemaOperativo": "IOS",
"almacenamientoGb": 128,
"id": 1,
"nombre": "Iphone 17",
"precio": 500000.0,
"stock": 70,
"categoria": "SMARTPHONE",
"estado": "NUEVO"
}

### Archivo CSV
Nombre: productos.csv
Lo mismo que el archivo JSON pero formateado para CSV

Ejemplo de contenido:

id;nombre;precio;stock;categoria;estado;tipoClase;ram;almacenamiento;so;tipoAccesorio;colorAccesorio
1;Iphone 17;500000.0;70;SMARTPHONE;NUEVO;SMARTPHONE;;128;IOS;;

---

### Archivo TXT
- Nombre: productostextoYYYY-MM-DD_HH-mm-ss.txt
- Archivo legible
- Incluye los atributos del producto

Ejemplo de contenido:

[productostexto2025-12-16_18-04-35.txt](https://github.com/user-attachments/files/24200319/productostexto2025-12-16_18-04-35.txt)
Productos mostrados en pantalla

ID   Nombre          Precio     Stock   Categoría  Estado             RAM    Alm(GB)    SO           TipoAccesorio   Color     
=============================================================================================================================
1    Iphone 17       1000,00    10      SMARTPHONE NUEVO              -      120        IOS          -               -         
2    Laptop Lenovo   500,00     40      LAPTOP     REACONDICIONADO    16     1000       -            -               -         
3    Funda Iphone 17 20,00      100     ACCESORIO  NUEVO              -      -          -            Funda           Negro     
4    Samsung Galaxy  200,00     10      SMARTPHONE USADO              -      64         Android      -               -         


