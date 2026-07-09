-- ============================================
-- SCRIPT: gestion_notas.sql
-- Sistema de Gestión de Notas por Curso (con promedio ponderado)
-- ============================================

CREATE DATABASE IF NOT EXISTS gestion_notas;
USE gestion_notas;

CREATE TABLE notas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_estudiante VARCHAR(20) NOT NULL,
    nombres VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    curso VARCHAR(60) NOT NULL,

    -- Componentes de la evaluación (escala 0-20)
    examen1 DECIMAL(4,2) NOT NULL,
    examen2 DECIMAL(4,2) NOT NULL,
    practica1 DECIMAL(4,2) NOT NULL,
    practica2 DECIMAL(4,2) NOT NULL,
    practica3 DECIMAL(4,2) NOT NULL,
    practica4 DECIMAL(4,2) NOT NULL,
    nota_actitudinal DECIMAL(4,2) NOT NULL,

    -- Promedio final calculado: (examenes*0.5) + (practicas*0.4) + (actitudinal*0.1)
    promedio_final DECIMAL(4,2) NOT NULL,

    fecha_registro DATE,
    observaciones VARCHAR(200),
    activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos de prueba (promedio_final ya calculado con la fórmula 50%/40%/10%)
INSERT INTO notas (codigo_estudiante, nombres, apellidos, curso,
                    examen1, examen2, practica1, practica2, practica3, practica4,
                    nota_actitudinal, promedio_final, fecha_registro, observaciones)
VALUES
('EST001', 'Juan', 'Pérez', 'Matemática I',
 14.00, 16.00, 15.00, 17.00, 13.00, 16.00, 18.00, 15.35, '2026-06-10', 'Buen desempeño'),
('EST002', 'María', 'González', 'Programación I',
 18.00, 19.00, 17.00, 18.00, 19.00, 18.00, 20.00, 18.35, '2026-06-11', 'Excelente'),
('EST003', 'Carlos', 'Rodríguez', 'Base de Datos',
 10.00, 11.00, 12.00, 10.00, 11.00, 13.00, 14.00, 11.35, '2026-06-12', 'Debe reforzar'),
('EST004', 'Ana', 'Martínez', 'Redes de Computadoras',
 16.00, 15.00, 16.00, 17.00, 15.00, 16.00, 17.00, 15.85, '2026-06-13', 'Muy consistente');

SELECT * FROM notas;
