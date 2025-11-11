-- ======================================================
-- Base de datos simple para el sistema SideSeven
-- Estructura: una sola tabla de ventas (una venta = un producto)
-- ======================================================

-- Crear la base de datos
DROP DATABASE IF EXISTS sideseven_db;
CREATE DATABASE sideseven_db;
USE sideseven_db;

-- ------------------------------------------------------
-- TABLA: productos
-- ------------------------------------------------------
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    categoria VARCHAR(100),
    precio DOUBLE NOT NULL,
    stock INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------
-- TABLA: clientes
-- ------------------------------------------------------
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------
-- TABLA: ventas (una venta = un producto vendido)
-- ------------------------------------------------------
CREATE TABLE ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_producto INT NOT NULL,
    fecha BIGINT NOT NULL,
    total DOUBLE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------
-- DATOS DE EJEMPLO
-- ------------------------------------------------------
INSERT INTO productos (nombre, categoria, precio, stock) VALUES
('One Piece Vol. 1', 'Manga', 8.99, 50),
('Batman: The Dark Knight', 'Comic', 15.99, 30),
('Dungeons & Dragons - Manual del Jugador', 'Rol', 49.99, 20),
('Figura Naruto Uzumaki', 'Figura', 29.99, 15),
('Attack on Titan Vol. 1', 'Manga', 9.99, 40);

INSERT INTO clientes (nombre, direccion) VALUES
('Martín Sánchez', 'Calle Mayor 123, Madrid'),
('Nuria Calo', 'Avenida Libertad 45, Barcelona'),
('Leo Ces', 'Plaza España 7, Valencia');

SELECT 'Base de datos creada correctamente' AS Resultado;
