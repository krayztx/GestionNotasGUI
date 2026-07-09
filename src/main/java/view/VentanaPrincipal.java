package view;

import com.formdev.flatlaf.FlatDarkLaf;
import controller.NotaController;
import model.Nota;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * Ventana principal del sistema de gestión de notas
 * Interfaz gráfica con Swing + FlatLaf (tema oscuro)
 *
 * Promedio final = 50% (promedio de 2 exámenes) + 40% (promedio de 4 prácticas) + 10% (nota actitudinal)
 */
public class VentanaPrincipal extends JFrame {

    // ================= Paleta de colores =================
    private static final Color COLOR_FONDO      = new Color(30, 30, 46);
    private static final Color COLOR_PANEL       = new Color(38, 38, 58);
    private static final Color COLOR_TEXTO       = new Color(230, 230, 240);
    private static final Color COLOR_ACENTO      = new Color(137, 180, 250);

    private static final Color COLOR_AGREGAR     = new Color(46, 204, 113);
    private static final Color COLOR_ACTUALIZAR  = new Color(52, 152, 219);
    private static final Color COLOR_ELIMINAR    = new Color(231, 76, 60);
    private static final Color COLOR_LIMPIAR     = new Color(108, 112, 134);
    private static final Color COLOR_BUSCAR      = new Color(148, 108, 245);
    private static final Color COLOR_RECARGAR    = new Color(46, 204, 113);
    private static final Color COLOR_SALIR       = new Color(231, 76, 60);
    private static final Color COLOR_CALCULAR    = new Color(241, 196, 15);
    private static final Color COLOR_REPORTE     = new Color(52, 152, 219);
    private static final Color COLOR_APROBADO    = new Color(46, 204, 113);
    private static final Color COLOR_DESAPROBADO = new Color(231, 76, 60);

    private static final Font FUENTE_LABEL  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FUENTE_BOTON  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FUENTE_TABLA  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 14);

    private NotaController controller;

    // Campos del formulario — datos del estudiante
    private JTextField txtId;
    private JTextField txtCodigo;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtCurso;

    // Campos del formulario — componentes de evaluación
    private JTextField txtExamen1;
    private JTextField txtExamen2;
    private JTextField txtPractica1;
    private JTextField txtPractica2;
    private JTextField txtPractica3;
    private JTextField txtPractica4;
    private JTextField txtActitudinal;
    private JTextField txtPromedioFinal; // solo lectura, se autocalcula

    private JTextField txtFecha;
    private JTextField txtObservaciones;
    private JTextField txtBuscar;

    // Botones
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnCalcular;
    private JButton btnBuscar;
    private JButton btnRecargar;
    private JButton btnDesaprobados;
    private JButton btnPromedioCurso;
    private JButton btnSalir;

    // Tabla
    private JTable tablaNotas;
    private DefaultTableModel modeloTabla;

    private JLabel lblTotalRegistros;

    public VentanaPrincipal() {
        controller = new NotaController();
        initComponents();
        cargarDatos();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Sistema de Gestión de Notas - CRUD");
        setSize(1100, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_FONDO);

        JPanel panelPrincipal = new JPanel(new BorderLayout(12, 12));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(COLOR_FONDO);

        // ================= Panel del formulario =================
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_PANEL);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                estilizarTitulo("Datos de la Evaluación"),
                new EmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Fila 0: ID / Código ---
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(crearLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtId = crearCampoTexto(10);
        txtId.setEditable(false);
        txtId.setBackground(new Color(50, 50, 70));
        panelFormulario.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panelFormulario.add(crearLabel("Código Est.:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        txtCodigo = crearCampoTexto(15);
        panelFormulario.add(txtCodigo, gbc);

        // --- Fila 1: Nombres / Apellidos ---
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(crearLabel("Nombres:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtNombres = crearCampoTexto(20);
        panelFormulario.add(txtNombres, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panelFormulario.add(crearLabel("Apellidos:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1;
        txtApellidos = crearCampoTexto(20);
        panelFormulario.add(txtApellidos, gbc);

        // --- Fila 2: Curso ---
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(crearLabel("Curso:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 3;
        txtCurso = crearCampoTexto(30);
        panelFormulario.add(txtCurso, gbc);
        gbc.gridwidth = 1;

        // --- Separador visual: componentes de evaluación ---
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 4;
        JLabel lblSeparador = crearLabel("— Componentes de la Evaluación (escala 0-20) —");
        lblSeparador.setForeground(COLOR_ACENTO);
        lblSeparador.setFont(FUENTE_TITULO);
        panelFormulario.add(lblSeparador, gbc);
        gbc.gridwidth = 1;

        // --- Fila 4: Examen 1 / Examen 2 (50%) ---
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(crearLabel("Examen 1 (50%):"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        txtExamen1 = crearCampoTexto(10);
        panelFormulario.add(txtExamen1, gbc);

        gbc.gridx = 2; gbc.gridy = 4;
        panelFormulario.add(crearLabel("Examen 2 (50%):"), gbc);
        gbc.gridx = 3; gbc.gridy = 4;
        txtExamen2 = crearCampoTexto(10);
        panelFormulario.add(txtExamen2, gbc);

        // --- Fila 5: Práctica 1 / Práctica 2 (40%) ---
        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(crearLabel("Práctica 1 (40%):"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        txtPractica1 = crearCampoTexto(10);
        panelFormulario.add(txtPractica1, gbc);

        gbc.gridx = 2; gbc.gridy = 5;
        panelFormulario.add(crearLabel("Práctica 2 (40%):"), gbc);
        gbc.gridx = 3; gbc.gridy = 5;
        txtPractica2 = crearCampoTexto(10);
        panelFormulario.add(txtPractica2, gbc);

        // --- Fila 6: Práctica 3 / Práctica 4 (40%) ---
        gbc.gridx = 0; gbc.gridy = 6;
        panelFormulario.add(crearLabel("Práctica 3 (40%):"), gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        txtPractica3 = crearCampoTexto(10);
        panelFormulario.add(txtPractica3, gbc);

        gbc.gridx = 2; gbc.gridy = 6;
        panelFormulario.add(crearLabel("Práctica 4 (40%):"), gbc);
        gbc.gridx = 3; gbc.gridy = 6;
        txtPractica4 = crearCampoTexto(10);
        panelFormulario.add(txtPractica4, gbc);

        // --- Fila 7: Nota actitudinal (10%) / Fecha ---
        gbc.gridx = 0; gbc.gridy = 7;
        panelFormulario.add(crearLabel("Actitudinal (10%):"), gbc);
        gbc.gridx = 1; gbc.gridy = 7;
        txtActitudinal = crearCampoTexto(10);
        panelFormulario.add(txtActitudinal, gbc);

        gbc.gridx = 2; gbc.gridy = 7;
        panelFormulario.add(crearLabel("Fecha Registro:"), gbc);
        gbc.gridx = 3; gbc.gridy = 7;
        txtFecha = crearCampoTexto(15);
        txtFecha.setToolTipText("Formato: YYYY-MM-DD");
        panelFormulario.add(txtFecha, gbc);

        // --- Fila 8: Observaciones ---
        gbc.gridx = 0; gbc.gridy = 8;
        panelFormulario.add(crearLabel("Observaciones:"), gbc);
        gbc.gridx = 1; gbc.gridy = 8;
        gbc.gridwidth = 3;
        txtObservaciones = crearCampoTexto(30);
        panelFormulario.add(txtObservaciones, gbc);
        gbc.gridwidth = 1;

        // --- Fila 9: Promedio final (solo lectura) + botón calcular ---
        gbc.gridx = 0; gbc.gridy = 9;
        JLabel lblPromedio = crearLabel("PROMEDIO FINAL:");
        lblPromedio.setFont(FUENTE_TITULO);
        lblPromedio.setForeground(COLOR_ACENTO);
        panelFormulario.add(lblPromedio, gbc);

        gbc.gridx = 1; gbc.gridy = 9;
        txtPromedioFinal = crearCampoTexto(10);
        txtPromedioFinal.setEditable(false);
        txtPromedioFinal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtPromedioFinal.setHorizontalAlignment(JTextField.CENTER);
        txtPromedioFinal.setBackground(new Color(50, 50, 70));
        txtPromedioFinal.setForeground(COLOR_ACENTO);
        panelFormulario.add(txtPromedioFinal, gbc);

        gbc.gridx = 2; gbc.gridy = 9;
        gbc.gridwidth = 2;
        btnCalcular = crearBoton("Calcular Promedio", COLOR_CALCULAR);
        btnCalcular.addActionListener(e -> calcularPromedioEnPantalla());
        panelFormulario.add(btnCalcular, gbc);
        gbc.gridwidth = 1;

        // --- Fila 10: Botones CRUD ---
        JPanel panelBotonesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        panelBotonesForm.setBackground(COLOR_PANEL);

        btnAgregar = crearBoton("Agregar", COLOR_AGREGAR);
        btnAgregar.addActionListener(e -> agregarNota());

        btnActualizar = crearBoton("Actualizar", COLOR_ACTUALIZAR);
        btnActualizar.addActionListener(e -> actualizarNota());

        btnEliminar = crearBoton("Eliminar", COLOR_ELIMINAR);
        btnEliminar.addActionListener(e -> eliminarNota());

        btnLimpiar = crearBoton("Limpiar", COLOR_LIMPIAR);
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotonesForm.add(btnAgregar);
        panelBotonesForm.add(btnActualizar);
        panelBotonesForm.add(btnEliminar);
        panelBotonesForm.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 10;
        gbc.gridwidth = 4;
        panelFormulario.add(panelBotonesForm, gbc);
        gbc.gridwidth = 1;

        // ================= Panel de la tabla =================
        JPanel panelTabla = new JPanel(new BorderLayout(8, 8));
        panelTabla.setBackground(COLOR_PANEL);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
                estilizarTitulo("Lista de Notas Registradas"),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBusqueda.setBackground(COLOR_PANEL);
        panelBusqueda.add(crearLabel("Buscar (código, nombre o apellido):"));

        txtBuscar = crearCampoTexto(18);
        panelBusqueda.add(txtBuscar);

        btnBuscar = crearBoton("Buscar", COLOR_BUSCAR);
        btnBuscar.addActionListener(e -> buscarNotas());

        btnRecargar = crearBoton("Recargar", COLOR_RECARGAR);
        btnRecargar.addActionListener(e -> cargarDatos());

        btnDesaprobados = crearBoton("Ver Desaprobados", COLOR_DESAPROBADO);
        btnDesaprobados.addActionListener(e -> verDesaprobados());

        btnPromedioCurso = crearBoton("Promedio del Curso", COLOR_REPORTE);
        btnPromedioCurso.addActionListener(e -> mostrarPromedioCurso());

        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnRecargar);
        panelBusqueda.add(btnDesaprobados);
        panelBusqueda.add(btnPromedioCurso);

        panelTabla.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"ID", "Código", "Nombres", "Apellidos", "Curso", "Prom. Final", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaNotas = new JTable(modeloTabla);
        tablaNotas.setRowHeight(28);
        tablaNotas.setFont(FUENTE_TABLA);
        tablaNotas.setBackground(COLOR_PANEL);
        tablaNotas.setForeground(COLOR_TEXTO);
        tablaNotas.setGridColor(new Color(60, 60, 84));
        tablaNotas.setSelectionBackground(COLOR_ACENTO.darker());
        tablaNotas.setSelectionForeground(Color.WHITE);
        tablaNotas.getTableHeader().setBackground(new Color(24, 24, 38));
        tablaNotas.getTableHeader().setForeground(COLOR_TEXTO);
        tablaNotas.getTableHeader().setFont(FUENTE_TITULO);
        tablaNotas.getTableHeader().setPreferredSize(new Dimension(0, 32));
        tablaNotas.setFillsViewportHeight(true);
        tablaNotas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarNota();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaNotas);
        scrollPane.getViewport().setBackground(COLOR_PANEL);
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.setBackground(COLOR_PANEL);
        lblTotalRegistros = new JLabel("Total: 0 registros");
        lblTotalRegistros.setFont(FUENTE_TITULO);
        lblTotalRegistros.setForeground(COLOR_ACENTO);
        panelTotal.add(lblTotalRegistros);
        panelTabla.add(panelTotal, BorderLayout.SOUTH);

        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(COLOR_FONDO);
        btnSalir = crearBoton("Salir", COLOR_SALIR);
        btnSalir.addActionListener(e -> salir());
        panelBotones.add(btnSalir);

        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        txtBuscar.addActionListener(e -> buscarNotas());
        getRootPane().setDefaultButton(btnAgregar);
    }

    // ================= Utilidades de estilo =================

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FUENTE_LABEL);
        label.setForeground(COLOR_TEXTO);
        return label;
    }

    private JTextField crearCampoTexto(int columnas) {
        JTextField campo = new JTextField(columnas);
        campo.setFont(FUENTE_LABEL);
        campo.setBackground(new Color(46, 46, 66));
        campo.setForeground(COLOR_TEXTO);
        campo.setCaretColor(COLOR_TEXTO);
        campo.putClientProperty("JComponent.roundRect", true);
        return campo;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(8, 18, 8, 18));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.putClientProperty("JButton.buttonType", "roundRect");
        boton.putClientProperty("JButton.arc", 16);
        boton.putClientProperty("Button.background", color);
        boton.putClientProperty("Button.hoverBackground", color.brighter());
        boton.putClientProperty("Button.pressedBackground", color.darker());
        return boton;
    }

    private TitledBorder estilizarTitulo(String titulo) {
        TitledBorder borde = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 84), 1, true), titulo);
        borde.setTitleFont(FUENTE_TITULO);
        borde.setTitleColor(COLOR_ACENTO);
        return borde;
    }

    // ================= MÉTODOS DE LA VISTA =================

    private void cargarDatos() {
        try {
            List<Nota> notas = controller.listarNotas();
            volcarEnTabla(notas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volcarEnTabla(List<Nota> notas) {
        modeloTabla.setRowCount(0);
        for (Nota n : notas) {
            Object[] fila = {
                    n.getId(),
                    n.getCodigoEstudiante(),
                    n.getNombres(),
                    n.getApellidos(),
                    n.getCurso(),
                    String.format("%.2f", n.getPromedioFinal()),
                    n.isAprobado() ? "Aprobado" : "Desaprobado"
            };
            modeloTabla.addRow(fila);
        }
        lblTotalRegistros.setText("Total: " + notas.size() + " registros");
    }

    /**
     * Lee los 7 componentes del formulario y calcula el promedio final,
     * mostrándolo en el campo de solo lectura. No guarda nada en la BD.
     */
    private double calcularPromedioEnPantalla() {
        try {
            double ex1 = parsearNota(txtExamen1.getText(), "Examen 1");
            double ex2 = parsearNota(txtExamen2.getText(), "Examen 2");
            double p1 = parsearNota(txtPractica1.getText(), "Práctica 1");
            double p2 = parsearNota(txtPractica2.getText(), "Práctica 2");
            double p3 = parsearNota(txtPractica3.getText(), "Práctica 3");
            double p4 = parsearNota(txtPractica4.getText(), "Práctica 4");
            double act = parsearNota(txtActitudinal.getText(), "Nota actitudinal");

            Nota temporal = new Nota();
            temporal.setExamen1(ex1);
            temporal.setExamen2(ex2);
            temporal.setPractica1(p1);
            temporal.setPractica2(p2);
            temporal.setPractica3(p3);
            temporal.setPractica4(p4);
            temporal.setNotaActitudinal(act);

            double promedio = temporal.calcularPromedioFinal();
            txtPromedioFinal.setText(String.format("%.2f", promedio));
            txtPromedioFinal.setForeground(promedio >= Nota.NOTA_MINIMA_APROBATORIA ? COLOR_APROBADO : COLOR_DESAPROBADO);
            return promedio;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    private double parsearNota(String texto, String nombreCampo) {
        texto = texto == null ? "" : texto.trim();
        if (texto.isEmpty()) {
            throw new NumberFormatException("Ingrese un valor para: " + nombreCampo);
        }
        double valor;
        try {
            valor = Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(nombreCampo + " debe ser un número válido");
        }
        if (valor < 0 || valor > 20) {
            throw new NumberFormatException(nombreCampo + " debe estar entre 0 y 20");
        }
        return valor;
    }

    private void agregarNota() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombres = txtNombres.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String curso = txtCurso.getText().trim();
            String fecha = txtFecha.getText().trim();
            String observaciones = txtObservaciones.getText().trim();

            if (codigo.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()
                    || curso.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double ex1 = parsearNota(txtExamen1.getText(), "Examen 1");
            double ex2 = parsearNota(txtExamen2.getText(), "Examen 2");
            double p1 = parsearNota(txtPractica1.getText(), "Práctica 1");
            double p2 = parsearNota(txtPractica2.getText(), "Práctica 2");
            double p3 = parsearNota(txtPractica3.getText(), "Práctica 3");
            double p4 = parsearNota(txtPractica4.getText(), "Práctica 4");
            double act = parsearNota(txtActitudinal.getText(), "Nota actitudinal");

            controller.registrarNota(codigo, nombres, apellidos, curso,
                    ex1, ex2, p1, p2, p3, p4, act, fecha, observaciones);

            JOptionPane.showMessageDialog(this,
                    "Nota registrada exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarDatos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarNota() {
        try {
            String idStr = txtId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un registro de la tabla",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);

            double ex1 = parsearNota(txtExamen1.getText(), "Examen 1");
            double ex2 = parsearNota(txtExamen2.getText(), "Examen 2");
            double p1 = parsearNota(txtPractica1.getText(), "Práctica 1");
            double p2 = parsearNota(txtPractica2.getText(), "Práctica 2");
            double p3 = parsearNota(txtPractica3.getText(), "Práctica 3");
            double p4 = parsearNota(txtPractica4.getText(), "Práctica 4");
            double act = parsearNota(txtActitudinal.getText(), "Nota actitudinal");

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Actualizar este registro?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                controller.actualizarNota(id,
                        txtCodigo.getText().trim(),
                        txtNombres.getText().trim(),
                        txtApellidos.getText().trim(),
                        txtCurso.getText().trim(),
                        ex1, ex2, p1, p2, p3, p4, act,
                        txtFecha.getText().trim(),
                        txtObservaciones.getText().trim());

                JOptionPane.showMessageDialog(this,
                        "Registro actualizado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarNota() {
        try {
            String idStr = txtId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un registro de la tabla",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar la nota de:\n" +
                            txtNombres.getText() + " " + txtApellidos.getText() + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                controller.eliminarNota(id);

                JOptionPane.showMessageDialog(this,
                        "Registro eliminado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();
                cargarDatos();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarNotas() {
        try {
            String texto = txtBuscar.getText().trim();
            if (texto.isEmpty()) {
                cargarDatos();
                return;
            }
            List<Nota> resultado = controller.buscarPorEstudiante(texto);
            volcarEnTabla(resultado);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDesaprobados() {
        try {
            List<Nota> resultado = controller.listarDesaprobados();
            volcarEnTabla(resultado);
            if (resultado.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay estudiantes desaprobados. ¡Felicidades!",
                        "Sin desaprobados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarPromedioCurso() {
        String curso = txtCurso.getText().trim();
        if (curso.isEmpty()) {
            curso = JOptionPane.showInputDialog(this,
                    "Escriba el nombre exacto del curso:",
                    "Promedio del Curso", JOptionPane.QUESTION_MESSAGE);
            if (curso == null || curso.trim().isEmpty()) {
                return;
            }
            curso = curso.trim();
        }

        try {
            double promedio = controller.promedioGeneralPorCurso(curso);
            JOptionPane.showMessageDialog(this,
                    String.format("Promedio general del curso \"%s\": %.2f", curso, promedio),
                    "Promedio del Curso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarNota() {
        int fila = tablaNotas.getSelectedRow();
        if (fila >= 0) {
            int id = (int) tablaNotas.getValueAt(fila, 0);
            Nota n = controller.buscarNota(id);
            if (n == null) return;

            txtId.setText(String.valueOf(n.getId()));
            txtCodigo.setText(n.getCodigoEstudiante());
            txtNombres.setText(n.getNombres());
            txtApellidos.setText(n.getApellidos());
            txtCurso.setText(n.getCurso());
            txtExamen1.setText(String.valueOf(n.getExamen1()));
            txtExamen2.setText(String.valueOf(n.getExamen2()));
            txtPractica1.setText(String.valueOf(n.getPractica1()));
            txtPractica2.setText(String.valueOf(n.getPractica2()));
            txtPractica3.setText(String.valueOf(n.getPractica3()));
            txtPractica4.setText(String.valueOf(n.getPractica4()));
            txtActitudinal.setText(String.valueOf(n.getNotaActitudinal()));
            txtFecha.setText(n.getFechaRegistro());
            txtObservaciones.setText(n.getObservaciones() != null ? n.getObservaciones() : "");
            txtPromedioFinal.setText(String.format("%.2f", n.getPromedioFinal()));
            txtPromedioFinal.setForeground(n.isAprobado() ? COLOR_APROBADO : COLOR_DESAPROBADO);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtCodigo.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCurso.setText("");
        txtExamen1.setText("");
        txtExamen2.setText("");
        txtPractica1.setText("");
        txtPractica2.setText("");
        txtPractica3.setText("");
        txtPractica4.setText("");
        txtActitudinal.setText("");
        txtFecha.setText("");
        txtObservaciones.setText("");
        txtPromedioFinal.setText("");
        txtPromedioFinal.setForeground(COLOR_ACENTO);
        txtCodigo.requestFocus();
        tablaNotas.clearSelection();
    }

    private void salir() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        UIManager.put("Button.arc", 16);
        UIManager.put("Component.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.width", 12);

        if (!model.ConexionBD.probarConexion()) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a la base de datos.\n" +
                            "Verifique que MySQL esté corriendo.\n" +
                            "Verifique usuario y contraseña en ConexionBD.java",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
