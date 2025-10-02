package persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.ConexionBD;
import model.Pelicula;

public class PeliculaDAO {

    public List<Pelicula> buscarPeliculas(String titulo, String genero, int anioMin, int anioMax) {
        List<Pelicula> listaPeliculas = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Cartelera WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // FILTRO 1 titulo
        if (titulo != null && !titulo.trim().isEmpty()) {
            sql.append(" AND titulo LIKE ?");
            params.add("%" + titulo.trim() + "%");
        }

        // FILTRO 2 genero 
        if (genero != null && !genero.trim().isEmpty()) {
            sql.append(" AND genero LIKE ?");
            params.add("%" + genero.trim() + "%");
        }

        // FILTRO 3 rango de años
        if (anioMin > 0 && anioMax >= anioMin) {
            sql.append(" AND anio BETWEEN ? AND ?");
            params.add(anioMin);
            params.add(anioMax);
        } else if (anioMin > 0 && anioMax == 0) { 
            sql.append(" AND anio >= ?");
            params.add(anioMin);
        } else if (anioMin == 0 && anioMax > 0) {
            sql.append(" AND anio <= ?");
            params.add(anioMax);
        }

        sql.append(" ORDER BY titulo ASC"); 

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
             
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pelicula p = new Pelicula(
                         rs.getInt("id"), rs.getString("titulo"), rs.getString("director"),
                         rs.getInt("anio"), rs.getInt("duracion"), rs.getString("genero")
                    );
                    listaPeliculas.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar películas: " + e.getMessage());
        }
        return listaPeliculas;
    }

    public boolean agregarPelicula(Pelicula pelicula) {
        String sql = "INSERT INTO Cartelera (titulo, director, anio, duracion, genero) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getDirector());
            ps.setInt(3, pelicula.getAnio());
            ps.setInt(4, pelicula.getDuracion());
            ps.setString(5, pelicula.getGenero());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar película: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarPelicula(Pelicula pelicula) {
        String sql = "UPDATE Cartelera SET titulo = ?, director = ?, anio = ?, duracion = ?, genero = ? WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getDirector());
            ps.setInt(3, pelicula.getAnio());
            ps.setInt(4, pelicula.getDuracion());
            ps.setString(5, pelicula.getGenero());
            ps.setInt(6, pelicula.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar película: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPelicula(int id) {
        String sql = "DELETE FROM Cartelera WHERE id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar película: " + e.getMessage());
            return false;
        }
    }
}