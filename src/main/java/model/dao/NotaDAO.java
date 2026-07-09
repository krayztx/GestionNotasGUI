package model.dao;

import model.Nota;
import model.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Nota
 */
public class NotaDAO {

    // ========== CREATE ==========
    public boolean insertar(Nota nota) throws SQLException {
        String sql = "INSERT INTO notas (codigo_estudiante, nombres, apellidos, curso, "
                + "examen1, examen2, practica1, practica2, practica3, practica4, "
                + "nota_actitudinal, promedio_final, fecha_registro, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nota.getCodigoEstudiante());
            ps.setString(2, nota.getNombres());
            ps.setString(3, nota.getApellidos());
            ps.setString(4, nota.getCurso());
            ps.setDouble(5, nota.getExamen1());
            ps.setDouble(6, nota.getExamen2());
            ps.setDouble(7, nota.getPractica1());
            ps.setDouble(8, nota.getPractica2());
            ps.setDouble(9, nota.getPractica3());
            ps.setDouble(10, nota.getPractica4());
            ps.setDouble(11, nota.getNotaActitudinal());
            ps.setDouble(12, nota.getPromedioFinal());
            ps.setString(13, nota.getFechaRegistro());
            ps.setString(14, nota.getObservaciones());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    nota.setId(rs.getInt(1));
                }
                return true;
            }
            return false;
        }
    }

    // ========== READ ==========
    public List<Nota> listarTodos() throws SQLException {
        List<Nota> lista = new ArrayList<>();
        String sql = "SELECT * FROM notas WHERE activo = true ORDER BY id";

        try (Statement stmt = ConexionBD.getConexion().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearNota(rs));
            }
        }
        return lista;
    }

    public Nota buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM notas WHERE id = ? AND activo = true";

        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearNota(rs);
            }
            return null;
        }
    }

    public List<Nota> buscarPorEstudiante(String texto) throws SQLException {
        List<Nota> lista = new ArrayList<>();
        String sql = "SELECT * FROM notas WHERE (nombres LIKE ? OR apellidos LIKE ? "
                + "OR codigo_estudiante LIKE ?) AND activo = true";

        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            String patron = "%" + texto + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearNota(rs));
            }
        }
        return lista;
    }

    /**
     * Lista únicamente los registros desaprobados (promedio_final < 10.5)
     */
    public List<Nota> listarDesaprobados() throws SQLException {
        List<Nota> lista = new ArrayList<>();
        String sql = "SELECT * FROM notas WHERE promedio_final < 10.5 AND activo = true ORDER BY promedio_final";

        try (Statement stmt = ConexionBD.getConexion().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearNota(rs));
            }
        }
        return lista;
    }

    /**
     * Calcula el promedio general del curso (promedio de todos los promedio_final)
     */
    public double promedioGeneralPorCurso(String curso) throws SQLException {
        String sql = "SELECT AVG(promedio_final) AS promedio FROM notas WHERE curso = ? AND activo = true";

        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, curso);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("promedio");
            }
            return 0.0;
        }
    }

    // ========== UPDATE ==========
    public boolean actualizar(Nota nota) throws SQLException {
        String sql = "UPDATE notas SET codigo_estudiante = ?, nombres = ?, apellidos = ?, curso = ?, "
                + "examen1 = ?, examen2 = ?, practica1 = ?, practica2 = ?, practica3 = ?, practica4 = ?, "
                + "nota_actitudinal = ?, promedio_final = ?, fecha_registro = ?, observaciones = ? WHERE id = ?";

        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, nota.getCodigoEstudiante());
            ps.setString(2, nota.getNombres());
            ps.setString(3, nota.getApellidos());
            ps.setString(4, nota.getCurso());
            ps.setDouble(5, nota.getExamen1());
            ps.setDouble(6, nota.getExamen2());
            ps.setDouble(7, nota.getPractica1());
            ps.setDouble(8, nota.getPractica2());
            ps.setDouble(9, nota.getPractica3());
            ps.setDouble(10, nota.getPractica4());
            ps.setDouble(11, nota.getNotaActitudinal());
            ps.setDouble(12, nota.getPromedioFinal());
            ps.setString(13, nota.getFechaRegistro());
            ps.setString(14, nota.getObservaciones());
            ps.setInt(15, nota.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // ========== DELETE (lógico) ==========
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE notas SET activo = false WHERE id = ?";

        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int contarRegistros() throws SQLException {
        String sql = "SELECT COUNT(*) FROM notas WHERE activo = true";
        try (Statement stmt = ConexionBD.getConexion().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    // ========== Utilidad interna ==========
    private Nota mapearNota(ResultSet rs) throws SQLException {
        Nota n = new Nota();
        n.setId(rs.getInt("id"));
        n.setCodigoEstudiante(rs.getString("codigo_estudiante"));
        n.setNombres(rs.getString("nombres"));
        n.setApellidos(rs.getString("apellidos"));
        n.setCurso(rs.getString("curso"));
        n.setExamen1(rs.getDouble("examen1"));
        n.setExamen2(rs.getDouble("examen2"));
        n.setPractica1(rs.getDouble("practica1"));
        n.setPractica2(rs.getDouble("practica2"));
        n.setPractica3(rs.getDouble("practica3"));
        n.setPractica4(rs.getDouble("practica4"));
        n.setNotaActitudinal(rs.getDouble("nota_actitudinal"));
        n.setPromedioFinal(rs.getDouble("promedio_final"));
        n.setFechaRegistro(rs.getString("fecha_registro"));
        n.setObservaciones(rs.getString("observaciones"));
        n.setActivo(rs.getBoolean("activo"));
        return n;
    }
}
