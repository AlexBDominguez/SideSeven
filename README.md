# SideSeven - Sistema de Gestión para Tienda Geek

Sistema de gestión de inventario, clientes y ventas para una tienda de cómics, mangas, manuales de rol y figuras de acción.

## 📋 Descripción del Proyecto

Este proyecto implementa un sistema completo de gestión empresarial en Java, desarrollado en dos fases:

### Fase 1: Almacenamiento con Ficheros (CSV)
- Gestión de productos, clientes y ventas
- Persistencia de datos en archivos CSV
- Operaciones CRUD completas para todas las entidades

### Fase 2: Migración a Base de Datos (MySQL)
- Conexión a base de datos MySQL mediante JDBC
- Migración automática de datos desde ficheros CSV
- Uso de transacciones para garantizar integridad de datos
- PreparedStatement para prevenir SQL injection

## 🚀 Características

### Gestión de Productos
- ✅ Añadir productos con **ID autogenerado**, nombre, categoría, precio y stock
- ✅ Actualizar información de productos
- ✅ Eliminar productos
- ✅ Buscar productos por ID
- ✅ Listar todos los productos

### Gestión de Clientes
- ✅ Añadir clientes con **ID autogenerado**, nombre y dirección
- ✅ Actualizar información de clientes
- ✅ Eliminar clientes
- ✅ Buscar clientes por ID
- ✅ Historial de compras por cliente
- ✅ Listar todos los clientes

### Gestión de Ventas
- ✅ Registrar ventas con **ID autogenerado** y múltiples productos
- ✅ Actualización automática de inventario
- ✅ Actualización automática del historial del cliente
- ✅ Cálculo automático del total
- ✅ Buscar ventas por ID
- ✅ Listar todas las ventas

## 🛠️ Tecnologías Utilizadas

- **Java 21** - Lenguaje de programación
- **Maven** - Gestión de dependencias
- **MySQL 8.x** - Base de datos relacional
- **JDBC** - Conectividad con base de datos
- **MySQL Connector/J 8.2.0** - Driver JDBC para MySQL

## 📦 Estructura del Proyecto

```
SideSeven/
├── src/main/java/
│   ├── app/
│   │   └── consolaApp.java          # Aplicación principal con menú
│   ├── dao/
│   │   ├── ProductoDAO.java         # DAO con ficheros CSV
│   │   ├── ClienteDAO.java          # DAO con ficheros CSV
│   │   ├── VentaDAO.java            # DAO con ficheros CSV
│   │   ├── ProductoDAODB.java       # DAO con base de datos
│   │   ├── ClienteDAODB.java        # DAO con base de datos
│   │   └── VentaDAODB.java          # DAO con base de datos
│   ├── db/
│   │   ├── DatabaseConnection.java  # Gestión de conexión a BD
│   │   ├── DatabaseInitializer.java # Creación de tablas
│   │   └── MigracionDatos.java      # Migración CSV → MySQL
│   ├── model/
│   │   ├── Producto.java            # Entidad Producto
│   │   ├── Cliente.java             # Entidad Cliente
│   │   └── Venta.java               # Entidad Venta
│   ├── service/
│   │   ├── ProductoService.java     # Lógica de negocio
│   │   ├── ClienteService.java      # Lógica de negocio
│   │   └── VentaService.java        # Lógica de negocio
│   ├── ui/
│   │   ├── ProductoUI.java          # Interfaz de usuario
│   │   ├── ClienteUI.java           # Interfaz de usuario
│   │   └── VentaUI.java             # Interfaz de usuario
│   └── main/
│       └── Main.java                # Punto de entrada
├── data/                            # Archivos CSV (Fase 1)
│   ├── productos.csv
│   ├── clientes.csv
│   └── ventas.csv
├── pom.xml                          # Configuración Maven
└── README.md                        # Este archivo
```

## ⚙️ Configuración e Instalación

### Requisitos Previos
- Java JDK 21 o superior
- Maven 3.6 o superior
- MySQL 8.0 o superior
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

### Paso 1: Clonar o descargar el proyecto

### Paso 2: Configurar MySQL

1. Instalar MySQL Server si no lo tienes
2. Crear la base de datos:

```sql
CREATE DATABASE sideseven_db;
```

3. Configurar las credenciales en `DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/sideseven_db";
private static final String USER = "root";
private static final String PASSWORD = "tu_contraseña"; // Cambiar aquí
```

### Paso 3: Compilar el proyecto

```bash
mvn clean compile
```

### Paso 4: Ejecutar la aplicación

```bash
mvn exec:java -Dexec.mainClass="main.Main"
```

O desde tu IDE, ejecutar la clase `Main.java`

## 📖 Uso del Sistema

### Fase 1: Trabajar con Ficheros CSV

1. Al iniciar la aplicación, selecciona las opciones del menú principal
2. Gestiona productos, clientes y ventas
3. Los datos se guardan automáticamente en archivos CSV en la carpeta `data/`

### Fase 2: Migrar a Base de Datos

1. Asegúrate de tener MySQL configurado y la base de datos creada
2. En el menú principal, selecciona la opción de migración
3. El sistema:
   - Creará las tablas necesarias automáticamente
   - Leerá los datos de los archivos CSV
   - Los insertará en la base de datos
   - Confirmará la migración exitosa

4. Después de la migración, el sistema puede trabajar con la base de datos

## 🗄️ Esquema de Base de Datos

### Tabla: productos
```sql
- id (INT, PRIMARY KEY)
- nombre (VARCHAR)
- categoria (VARCHAR)
- precio (DOUBLE)
- stock (INT)
```

### Tabla: clientes
```sql
- id (INT, PRIMARY KEY)
- nombre (VARCHAR)
- direccion (VARCHAR)
```

### Tabla: ventas
```sql
- id (INT, PRIMARY KEY)
- id_cliente (INT, FOREIGN KEY)
- fecha (BIGINT)
- total (DOUBLE)
```

### Tabla: detalle_ventas
```sql
- id_venta (INT, FOREIGN KEY)
- id_producto (INT, FOREIGN KEY)
- PRIMARY KEY (id_venta, id_producto)
```

### Tabla: historial_compras
```sql
- id_cliente (INT, FOREIGN KEY)
- id_venta (INT, FOREIGN KEY)
- PRIMARY KEY (id_cliente, id_venta)
```

## 🔒 Características de Seguridad

- ✅ Uso de **PreparedStatement** para prevenir SQL Injection
- ✅ **Transacciones** para garantizar integridad de datos
- ✅ **Manejo de excepciones** con try-catch-finally
- ✅ **Rollback automático** en caso de error durante transacciones
- ✅ Validación de datos de entrada

## 📝 Conceptos Implementados

### JDBC (Java Database Connectivity)
- Carga del driver: `Class.forName("com.mysql.cj.jdbc.Driver")`
- Conexión: `DriverManager.getConnection(url, user, password)`
- Statement y PreparedStatement para consultas
- ResultSet para procesar resultados
- Manejo de transacciones con commit/rollback

### Patrón DAO (Data Access Object)
- Separación de lógica de acceso a datos
- Dos implementaciones: CSV y Base de Datos
- Facilita el cambio entre fuentes de datos

### Diseño Orientado a Objetos
- Clases modelo (Producto, Cliente, Venta)
- Servicios para lógica de negocio
- Interfaces de usuario separadas
- Encapsulación y modularidad

## 🐛 Manejo de Errores

El sistema implementa manejo robusto de errores:
- Validación de entrada de usuario
- Try-catch para excepciones de I/O
- Try-catch para excepciones SQL
- Mensajes de error descriptivos
- Rollback de transacciones en caso de fallo

## 👥 Autor

Proyecto desarrollado para FP Superior Dual UDAM 2 - Acceso a Datos

## 📄 Licencia

Este proyecto es de uso educativo.

## 🔄 Posibles Mejoras

- [ ] Interfaz gráfica con JavaFX
- [ ] Reportes y estadísticas
- [ ] Búsqueda avanzada con filtros
- [ ] Exportación de datos a PDF/Excel
- [ ] Sistema de usuarios y autenticación
- [ ] Backup automático de datos
