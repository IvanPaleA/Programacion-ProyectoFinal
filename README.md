# Luxe Gelato - Sistema de Inventario y Ventas

## Descripción
Este es el proyecto final para la materia de Programación. Es un sistema de escritorio desarrollado en Java para gestionar el inventario y las ventas de una heladería ("Luxe Gelato"). El sistema permite registrar productos, buscar productos utilizando un Árbol Binario de Búsqueda (ABB), ordenarlos y registrar las ventas, guardando toda la información en una base de datos MySQL.

## Funcionalidades principales
*   **Login de usuarios:** Control de acceso al sistema.
*   **Gestión de Inventario (CRUD):** Agregar, modificar, buscar y eliminar productos (helados, paletas, etc.).
*   **Búsqueda de productos:** Permite buscar productos utilizando un Árbol Binario de Búsqueda (ABB).
*   **Ordenamiento de datos:** Ordenamiento del inventario por nombre o precio usando algoritmos como QuickSort y MergeSort.
*   **Módulo de ventas:** Permite registrar ventas y generar tickets.
*   **Bitácora:** Registro de los productos eliminados, incluyendo la razón de eliminación, la fecha y el usuario responsable.

## Estructura del Proyecto
El proyecto está organizado en paquetes para separar la lógica, como vimos en clase:
*   **`model`**: Contiene las clases del modelo de datos (como `Producto`).
*   **`view`**: Contiene todas las ventanas e interfaces gráficas hechas con Java Swing.
*   **`database`**: Contiene la clase de conexión a MySQL y los DAOs que ejecutan las consultas (SELECT, INSERT, etc.).
*   **`structures`**: Contiene la lógica de las estructuras de datos (como el Árbol ABB y los algoritmos de ordenamiento).

## Temas del curso aplicados
En este proyecto aplicamos los siguientes temas vistos durante el semestre:
*   POJO
*   DAO
*   Árboles Binarios de Búsqueda (ABB)
*   Listas
*   QuickSort
*   MergeSort
*   JDBC
*   Java Swing
*   JavaDoc

## Requisitos para ejecutar
Para correr este proyecto en tu computadora necesitas:
1.  **IDE:** Apache NetBeans (versiones recientes).
2.  **Base de Datos:** MySQL Server instalado y corriendo localmente (XAMPP, Workbench, etc.).
3.  **Librería:** Conector JDBC de MySQL (`mysql-connector-j`).

## Instrucciones básicas de configuración
Para que el proyecto funcione correctamente en tu máquina, sigue estos pasos:
1.  Abre MySQL y crea una base de datos llamada `heladeria` (puedes importar el script SQL que acompaña al proyecto si está disponible).
2.  Abre el proyecto en NetBeans.
3.  Ve al paquete `heladeria.database` y abre el archivo `ConexionBD.java`. Modifica el usuario (`root`) y la contraseña (`1234`) por los que tengas en tu instalación local de MySQL.
4.  En la ventana "Projects" de NetBeans, da clic derecho en la carpeta **Libraries** -> **Add JAR/Folder** y selecciona el archivo `.jar` del conector de MySQL.
5.  Da clic derecho sobre el proyecto y selecciona **Run** (o presiona F6).

## Usuario de prueba
Para ingresar al sistema una vez que esté corriendo, puedes usar:
*   **Usuario:** admin
*   **Contraseña:** 123

---
**Integrantes del equipo:**
*   Martinez Garcia Angel Ricardo
*   Pale Arenas Ivan
*   Saldaña Marlene
