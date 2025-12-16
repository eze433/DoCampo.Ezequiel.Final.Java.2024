package gestion;

import excepciones.DatoInvalidoException;
import excepciones.EntidadNoEncontradaException;

import clases.Producto;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GestorEntidades<T extends Producto> implements Repositorio<T>, Iterable<T> {

    private final List<T> lista = new ArrayList<>();

    @Override
    public void agregar(T entidad) throws DatoInvalidoException {
        Objects.requireNonNull(entidad, "Entidad nula");
        if (buscarPorId(entidad.getId()).isPresent()) {
            throw new DatoInvalidoException("Ya existe un producto con id " + entidad.getId());
        }
        lista.add(entidad);
    }

    @Override
    public Optional<T> buscarPorId(int id) {
        return lista.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
    }

    @Override
    public List<T> listar() {
        return new ArrayList<>(lista);
    }

    @Override
    public void actualizar(T entidad) throws EntidadNoEncontradaException {
        Optional<T> actual = buscarPorId(entidad.getId());
        if (actual.isEmpty()) {
            throw new EntidadNoEncontradaException("No se encuentra el id " + entidad.getId());
        }
        int index = lista.indexOf(actual.get());
        lista.set(index, entidad);
    }

    @Override
    public void eliminar(int id) throws EntidadNoEncontradaException {
        Optional<T> actual = buscarPorId(id);
        if (actual.isEmpty()) {
            throw new EntidadNoEncontradaException("No se encuentra el id " + id);
        }
        lista.remove(actual.get());
    }

    // iterator personalizado
    @Override
    public Iterator<T> iterator() {
        return new IteradorGestor();
    }

    private class IteradorGestor implements Iterator<T> {
        private int posicion = 0;

        @Override
        public boolean hasNext() {
            return posicion < lista.size();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return lista.get(posicion++);
        }
    }

    // filtros con wildcards y predicate
    public List<T> filtrar(Predicate<? super T> criterio) {
        return lista.stream()
                .filter(criterio::test)
                .collect(Collectors.toList());
    }

    // Filtro por nombre "tipo comodín" (contiene texto)
    public List<T> filtrarPorNombre(String texto) {
        String lower = texto.toLowerCase();
        return lista.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // interfaces funcionales
    // consumer
    public void aplicarAccion(Consumer<? super T> accion) {
        lista.forEach(accion);
    }

    // mapea a otra lista
    public <R> List<R> mapear(Function<? super T, ? extends R> mapper) {
        return lista.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    public static void imprimirNombres(List<? extends Producto> productos) {
        for (Producto p : productos) {
            System.out.println(p.getNombre());
        }
    }

    public static void copiarProductos(List<? extends Producto> origen, List<? super Producto> destino) {
        destino.addAll(origen);
    }
}
