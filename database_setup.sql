-- Script SQL para configurar la base de datos SideSeven
-- Ejecutar este script.

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS sideseven_db;

-- Usar la base de datos
USE sideseven_db;

-- Eliminar tablas si existen (para empezar limpio)
DROP TABLE IF EXISTS historial_compras;
DROP TABLE IF EXISTS detalle_ventas;
DROP TABLE IF EXISTS ventas;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS productos;

-- Crear tabla de productos
CREATE TABLE productos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    categoria VARCHAR(100),
    precio DOUBLE NOT NULL,
    stock INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Crear tabla de clientes
CREATE TABLE clientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Crear tabla de ventas
CREATE TABLE ventas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    fecha BIGINT NOT NULL,
    total DOUBLE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Crear tabla de detalle de ventas (relación muchos a muchos entre ventas y productos)
CREATE TABLE detalle_ventas (
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    PRIMARY KEY (id_venta, id_producto),
    FOREIGN KEY (id_venta) REFERENCES ventas(id) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Crear tabla de historial de compras (relación entre clientes y ventas)
CREATE TABLE historial_compras (
    id_cliente INT NOT NULL,
    id_venta INT NOT NULL,
    PRIMARY KEY (id_cliente, id_venta),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE,
    FOREIGN KEY (id_venta) REFERENCES ventas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insertar datos de ejemplo (opcional)
-- Productos de ejemplo (sin especificar ID)
INSERT INTO productos (nombre, categoria, precio, stock) VALUES
('One Piece Vol. 1', 'Manga', 8.99, 50),
('Batman: The Dark Knight', 'Comic', 15.99, 30),
('Dungeons & Dragons - Manual del Jugador', 'Rol', 49.99, 20),
('Figura Naruto Uzumaki', 'Figura', 29.99, 15),
('Attack on Titan Vol. 1', 'Manga', 9.99, 40);

-- Clientes de ejemplo (sin especificar ID)
INSERT INTO clientes (nombre, direccion) VALUES
('Martín Sánchez', 'Calle Mayor 123, Madrid'),
('Nuria Calo', 'Avenida Libertad 45, Barcelona'),
('Leo Ces', 'Plaza España 7, Valencia');

-- Mostrar las tablas creadas
SHOW TABLES;

-- Verificar la estructura de las tablas
DESCRIBE productos;
DESCRIBE clientes;
DESCRIBE ventas;
DESCRIBE detalle_ventas;
DESCRIBE historial_compras;

SELECT 'Base de datos configurada correctamente' AS Resultado;