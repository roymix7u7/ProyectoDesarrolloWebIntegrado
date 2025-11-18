-- =====================================================
-- Script de Creación de Base de Datos
-- Plataforma de Cursos Online
-- SQL Server 2019+
-- =====================================================

-- Crear la base de datos si no existe
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'cursosdb')
BEGIN
    CREATE DATABASE cursosdb;
END
GO

USE cursosdb;
GO

-- =====================================================
-- Tabla: usuarios
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'usuarios') AND type in (N'U'))
BEGIN
    CREATE TABLE usuarios (
        id_usuario INT IDENTITY(1,1) PRIMARY KEY,
        nombre_completo VARCHAR(150) NOT NULL,
        email VARCHAR(150) NOT NULL UNIQUE,
        password_hash VARCHAR(255) NOT NULL,
        telefono VARCHAR(20),
        tipo_usuario VARCHAR(50) NOT NULL,
        estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
        fecha_registro DATETIME NOT NULL DEFAULT GETDATE(),
        ultimo_acceso DATETIME,
        foto_perfil VARCHAR(255),
        CONSTRAINT CK_Usuario_TipoUsuario CHECK (tipo_usuario IN ('ADMIN', 'INSTRUCTOR', 'ESTUDIANTE', 'SOPORTE')),
        CONSTRAINT CK_Usuario_Estado CHECK (estado IN ('ACTIVO', 'INACTIVO', 'SUSPENDIDO'))
    );

    CREATE INDEX IX_Usuario_Email ON usuarios(email);
    CREATE INDEX IX_Usuario_TipoUsuario ON usuarios(tipo_usuario);
END
GO

-- =====================================================
-- Tabla: cursos
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'cursos') AND type in (N'U'))
BEGIN
    CREATE TABLE cursos (
        id_curso INT IDENTITY(1,1) PRIMARY KEY,
        titulo VARCHAR(200) NOT NULL,
        descripcion TEXT,
        categoria VARCHAR(100),
        precio DECIMAL(10,2) NOT NULL,
        duracion_horas INT,
        nivel VARCHAR(50),
        modalidad VARCHAR(50),
        estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
        fecha_creacion DATETIME NOT NULL DEFAULT GETDATE(),
        imagen_portada VARCHAR(255),
        requisitos_previos TEXT,
        CONSTRAINT CK_Curso_Nivel CHECK (nivel IN ('BASICO', 'INTERMEDIO', 'AVANZADO')),
        CONSTRAINT CK_Curso_Modalidad CHECK (modalidad IN ('ONLINE', 'PRESENCIAL', 'HIBRIDO')),
        CONSTRAINT CK_Curso_Estado CHECK (estado IN ('ACTIVO', 'INACTIVO', 'BORRADOR'))
    );

    CREATE INDEX IX_Curso_Categoria ON cursos(categoria);
    CREATE INDEX IX_Curso_Estado ON cursos(estado);
END
GO

-- =====================================================
-- Tabla: inscripciones
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'inscripciones') AND type in (N'U'))
BEGIN
    CREATE TABLE inscripciones (
        id_inscripcion INT IDENTITY(1,1) PRIMARY KEY,
        id_usuario INT NOT NULL,
        id_curso INT NOT NULL,
        fecha_inscripcion DATETIME NOT NULL DEFAULT GETDATE(),
        progreso INT DEFAULT 0,
        estado_inscripcion VARCHAR(50) NOT NULL DEFAULT 'EN_PROGRESO',
        fecha_completado DATETIME,
        CONSTRAINT FK_Inscripcion_Usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
        CONSTRAINT FK_Inscripcion_Curso FOREIGN KEY (id_curso) REFERENCES cursos(id_curso) ON DELETE CASCADE,
        CONSTRAINT CK_Inscripcion_Estado CHECK (estado_inscripcion IN ('EN_PROGRESO', 'COMPLETADO', 'CANCELADO')),
        CONSTRAINT CK_Inscripcion_Progreso CHECK (progreso >= 0 AND progreso <= 100),
        CONSTRAINT UQ_Usuario_Curso UNIQUE (id_usuario, id_curso)
    );

    CREATE INDEX IX_Inscripcion_Usuario ON inscripciones(id_usuario);
    CREATE INDEX IX_Inscripcion_Curso ON inscripciones(id_curso);
END
GO

-- =====================================================
-- Tabla: certificados
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'certificados') AND type in (N'U'))
BEGIN
    CREATE TABLE certificados (
        id_certificado INT IDENTITY(1,1) PRIMARY KEY,
        id_inscripcion INT NOT NULL UNIQUE,
        codigo_certificado VARCHAR(50) NOT NULL UNIQUE,
        fecha_emision DATETIME NOT NULL DEFAULT GETDATE(),
        url_pdf VARCHAR(255),
        verificable BIT NOT NULL DEFAULT 1,
        calificacion DECIMAL(5,2),
        CONSTRAINT FK_Certificado_Inscripcion FOREIGN KEY (id_inscripcion) REFERENCES inscripciones(id_inscripcion) ON DELETE CASCADE,
        CONSTRAINT CK_Certificado_Calificacion CHECK (calificacion >= 0 AND calificacion <= 100)
    );

    CREATE INDEX IX_Certificado_Codigo ON certificados(codigo_certificado);
END
GO

-- =====================================================
-- Tabla: compras
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'compras') AND type in (N'U'))
BEGIN
    CREATE TABLE compras (
        id_compra INT IDENTITY(1,1) PRIMARY KEY,
        id_usuario INT NOT NULL,
        id_curso INT NOT NULL,
        fecha_compra DATETIME NOT NULL DEFAULT GETDATE(),
        monto_total DECIMAL(10,2) NOT NULL,
        estado_compra VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
        metodo_pago VARCHAR(50),
        comprobante VARCHAR(255),
        descuento DECIMAL(10,2) DEFAULT 0,
        CONSTRAINT FK_Compra_Usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
        CONSTRAINT FK_Compra_Curso FOREIGN KEY (id_curso) REFERENCES cursos(id_curso),
        CONSTRAINT CK_Compra_Estado CHECK (estado_compra IN ('PENDIENTE', 'COMPLETADA', 'CANCELADA', 'REEMBOLSADA'))
    );

    CREATE INDEX IX_Compra_Usuario ON compras(id_usuario);
    CREATE INDEX IX_Compra_Estado ON compras(estado_compra);
END
GO

-- =====================================================
-- Tabla: pagos
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'pagos') AND type in (N'U'))
BEGIN
    CREATE TABLE pagos (
        id_pago INT IDENTITY(1,1) PRIMARY KEY,
        id_compra INT NOT NULL,
        fecha_pago DATETIME NOT NULL DEFAULT GETDATE(),
        monto DECIMAL(10,2) NOT NULL,
        estado_pago VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
        transaccion_id VARCHAR(100),
        pasarela VARCHAR(50),
        respuesta_pasarela TEXT,
        CONSTRAINT FK_Pago_Compra FOREIGN KEY (id_compra) REFERENCES compras(id_compra) ON DELETE CASCADE,
        CONSTRAINT CK_Pago_Estado CHECK (estado_pago IN ('PENDIENTE', 'APROBADO', 'RECHAZADO', 'REEMBOLSADO'))
    );

    CREATE INDEX IX_Pago_Compra ON pagos(id_compra);
    CREATE INDEX IX_Pago_Estado ON pagos(estado_pago);
END
GO

-- =====================================================
-- Tabla: tickets_soporte
-- =====================================================
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'tickets_soporte') AND type in (N'U'))
BEGIN
    CREATE TABLE tickets_soporte (
        id_ticket INT IDENTITY(1,1) PRIMARY KEY,
        id_usuario INT NOT NULL,
        asunto VARCHAR(200) NOT NULL,
        descripcion TEXT NOT NULL,
        prioridad VARCHAR(20) NOT NULL DEFAULT 'MEDIA',
        estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTO',
        fecha_creacion DATETIME NOT NULL DEFAULT GETDATE(),
        fecha_resolucion DATETIME,
        CONSTRAINT FK_Ticket_Usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
        CONSTRAINT CK_Ticket_Prioridad CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA', 'URGENTE')),
        CONSTRAINT CK_Ticket_Estado CHECK (estado IN ('ABIERTO', 'EN_PROCESO', 'RESUELTO', 'CERRADO'))
    );

    CREATE INDEX IX_Ticket_Usuario ON tickets_soporte(id_usuario);
    CREATE INDEX IX_Ticket_Estado ON tickets_soporte(estado);
END
GO

-- =====================================================
-- Datos de Prueba (Opcional)
-- =====================================================

-- Usuario Administrador (password: admin123)
IF NOT EXISTS (SELECT * FROM usuarios WHERE email = 'admin@cursosplatform.com')
BEGIN
    INSERT INTO usuarios (nombre_completo, email, password_hash, tipo_usuario, estado)
    VALUES ('Administrador Sistema', 'admin@cursosplatform.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 'ACTIVO');
END
GO

-- Usuario Instructor (password: instructor123)
IF NOT EXISTS (SELECT * FROM usuarios WHERE email = 'instructor@cursosplatform.com')
BEGIN
    INSERT INTO usuarios (nombre_completo, email, password_hash, tipo_usuario, estado)
    VALUES ('Carlos Instructor', 'instructor@cursosplatform.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'INSTRUCTOR', 'ACTIVO');
END
GO

-- Cursos de ejemplo
IF NOT EXISTS (SELECT * FROM cursos WHERE titulo = 'Java Avanzado')
BEGIN
    INSERT INTO cursos (titulo, descripcion, categoria, precio, duracion_horas, nivel, modalidad, estado)
    VALUES
    ('Java Avanzado', 'Curso completo de Java desde cero hasta avanzado', 'Programación', 99.99, 40, 'AVANZADO', 'ONLINE', 'ACTIVO'),
    ('Python para Data Science', 'Aprende Python aplicado a ciencia de datos', 'Data Science', 149.99, 60, 'INTERMEDIO', 'ONLINE', 'ACTIVO'),
    ('Spring Boot Masterclass', 'Domina Spring Boot 3 desde cero', 'Programación', 129.99, 50, 'INTERMEDIO', 'ONLINE', 'ACTIVO');
END
GO

PRINT 'Base de datos creada exitosamente';
PRINT 'Tablas: usuarios, cursos, inscripciones, certificados, compras, pagos, tickets_soporte';
PRINT 'Usuario admin: admin@cursosplatform.com / admin123';
PRINT 'Usuario instructor: instructor@cursosplatform.com / instructor123';
GO
