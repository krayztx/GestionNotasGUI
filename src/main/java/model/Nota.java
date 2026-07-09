package model;

/**
 * Clase que representa la entidad Nota: el registro de evaluación completo
 * de un estudiante en un curso, calculado con la fórmula:
 *
 *   Promedio Final = (Promedio Exámenes * 0.50)
 *                   + (Promedio Prácticas * 0.40)
 *                   + (Nota Actitudinal * 0.10)
 *
 * donde:
 *   Promedio Exámenes  = (examen1 + examen2) / 2
 *   Promedio Prácticas = (practica1 + practica2 + practica3 + practica4) / 4
 */
public class Nota {

    private int id;
    private String codigoEstudiante;
    private String nombres;
    private String apellidos;
    private String curso;

    private double examen1;
    private double examen2;
    private double practica1;
    private double practica2;
    private double practica3;
    private double practica4;
    private double notaActitudinal;

    private double promedioFinal;

    private String fechaRegistro;
    private String observaciones;
    private boolean activo;

    // Pesos de la fórmula (fáciles de ajustar si el curso cambia de política de evaluación)
    public static final double PESO_EXAMENES = 0.50;
    public static final double PESO_PRACTICAS = 0.40;
    public static final double PESO_ACTITUDINAL = 0.10;
    public static final double NOTA_MINIMA_APROBATORIA = 10.5;

    public Nota() {}

    public Nota(String codigoEstudiante, String nombres, String apellidos, String curso,
                double examen1, double examen2,
                double practica1, double practica2, double practica3, double practica4,
                double notaActitudinal, String fechaRegistro, String observaciones) {
        this.codigoEstudiante = codigoEstudiante;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.curso = curso;
        this.examen1 = examen1;
        this.examen2 = examen2;
        this.practica1 = practica1;
        this.practica2 = practica2;
        this.practica3 = practica3;
        this.practica4 = practica4;
        this.notaActitudinal = notaActitudinal;
        this.fechaRegistro = fechaRegistro;
        this.observaciones = observaciones;
        this.activo = true;
        this.promedioFinal = calcularPromedioFinal();
    }

    public Nota(int id, String codigoEstudiante, String nombres, String apellidos, String curso,
                double examen1, double examen2,
                double practica1, double practica2, double practica3, double practica4,
                double notaActitudinal, double promedioFinal,
                String fechaRegistro, String observaciones, boolean activo) {
        this.id = id;
        this.codigoEstudiante = codigoEstudiante;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.curso = curso;
        this.examen1 = examen1;
        this.examen2 = examen2;
        this.practica1 = practica1;
        this.practica2 = practica2;
        this.practica3 = practica3;
        this.practica4 = practica4;
        this.notaActitudinal = notaActitudinal;
        this.promedioFinal = promedioFinal;
        this.fechaRegistro = fechaRegistro;
        this.observaciones = observaciones;
        this.activo = activo;
    }

    // ========== GETTERS Y SETTERS ==========
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public double getExamen1() { return examen1; }
    public void setExamen1(double examen1) { this.examen1 = examen1; }

    public double getExamen2() { return examen2; }
    public void setExamen2(double examen2) { this.examen2 = examen2; }

    public double getPractica1() { return practica1; }
    public void setPractica1(double practica1) { this.practica1 = practica1; }

    public double getPractica2() { return practica2; }
    public void setPractica2(double practica2) { this.practica2 = practica2; }

    public double getPractica3() { return practica3; }
    public void setPractica3(double practica3) { this.practica3 = practica3; }

    public double getPractica4() { return practica4; }
    public void setPractica4(double practica4) { this.practica4 = practica4; }

    public double getNotaActitudinal() { return notaActitudinal; }
    public void setNotaActitudinal(double notaActitudinal) { this.notaActitudinal = notaActitudinal; }

    public double getPromedioFinal() { return promedioFinal; }
    public void setPromedioFinal(double promedioFinal) { this.promedioFinal = promedioFinal; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    // ========== CÁLCULOS DE PROMEDIOS ==========

    public double getPromedioExamenes() {
        return redondear((examen1 + examen2) / 2.0);
    }

    public double getPromedioPracticas() {
        return redondear((practica1 + practica2 + practica3 + practica4) / 4.0);
    }

    /**
     * Calcula el promedio final ponderado:
     * 50% exámenes + 40% prácticas + 10% nota actitudinal
     */
    public double calcularPromedioFinal() {
        double resultado = (getPromedioExamenes() * PESO_EXAMENES)
                          + (getPromedioPracticas() * PESO_PRACTICAS)
                          + (notaActitudinal * PESO_ACTITUDINAL);
        return redondear(resultado);
    }

    public boolean isAprobado() {
        return promedioFinal >= NOTA_MINIMA_APROBATORIA;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return String.format("%d | %s | %s %s | %s | Promedio: %.2f",
                id, codigoEstudiante, nombres, apellidos, curso, promedioFinal);
    }
}
