package gestion;

import java.util.List;
import java.util.Optional;

public interface Repositorio<T> {
    void agregar(T entidad) throws Exception;
    Optional<T> buscarPorId(int id);
    List<T> listar();
    void actualizar(T entidad) throws Exception;
    void eliminar(int id) throws Exception;
}