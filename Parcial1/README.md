# QuindíoExpress — Sistema de Gestión de Operaciones de Distribución

Sistema desarrollado en Java para gestionar y optimizar las operaciones de distribución de paquetes de **QuindíoExpress** en el departamento del Quindío. El proyecto forma parte del **Parcial 1 de la asignatura Estructuras de Datos (2026-2)** de la Universidad del Quindío.

La aplicación está orientada a la gestión de paquetes durante las diferentes etapas del proceso logístico, desde su recepción y registro hasta su clasificación, priorización, asignación a repartidores y entrega.

##  Objetivo del proyecto

Desarrollar una aplicación que permita administrar las operaciones de un centro de distribución mediante el uso de estructuras de datos, algoritmos de búsqueda, técnicas recursivas y estrategias de organización de información.

### Funcionalidades principales

-  **Recepción y registro:** gestionar el ingreso de paquetes al sistema.
-  **Clasificación:** organizar los paquetes según sus características.
-  **Priorización:** establecer el orden de atención de los paquetes según su nivel de prioridad.
-  **Búsqueda:** localizar paquetes mediante los algoritmos implementados.
-  **Asignación:** gestionar la asignación de paquetes a los repartidores.
-  **Gestión de entregas:** administrar el proceso de entrega de los paquetes.
-  **Pruebas:** verificar el funcionamiento de las funcionalidades mediante casos de prueba ejecutables.

##  Integrantes del equipo

| Integrante |
|---|
| Jose Gabriel Paguay Imbacuan |
| Natalia Estefany Burbano|
| Juan Manuel Mejia|

##  Tecnologías y herramientas

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje de programación |
| Apache Maven | Gestión del proyecto y compilación |
| IntelliJ IDEA | Entorno de desarrollo recomendado |
| Git y GitHub | Control de versiones y colaboración |
| `Prueba` | Clase auxiliar personalizada para las verificaciones |
| `TodasLasPruebas` | Ejecutor del conjunto de pruebas |

> **Nota:** Las pruebas utilizan un mecanismo personalizado de verificación, sin depender de JUnit.

##  Estructura del proyecto

La organización del código sigue una separación por responsabilidades:

```text
QuindioExpress/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── co/
│   │           └── edu/
│   │               └── uniquindio/
│   │                   ├── app/            # Punto de entrada de la aplicación
│   │                   ├── algoritmos/     # Algoritmos recursivos, búsqueda binaria y D&C
│   │                   ├── comparadores/   # Comparadores para ordenar paquetes
│   │                   ├── datos/          # Datos iniciales del sistema
│   │                   ├── estructuras/    # Lista enlazada e interfaz genérica propia
│   │                   ├── modelo/         # Entidades Paquete y Repartidor
│   │                   └── servicio/       # Gestión del sistema y centro de distribución
│   └── test/
│       └── java/
│           └── co/
│               └── edu/
│                   └── uniquindio/
│                       ├── prueba/         # Casos de prueba y ejecutor principal
│                       └── util/           # Clase auxiliar Prueba
├── pom.xml                                 # Configuración de Maven
└── README.md                               # Documentación del proyecto
```

##  Requisitos previos

Antes de ejecutar el proyecto, asegúrate de contar con:

- **JDK 17** instalado y configurado.
- **Apache Maven** instalado o integrado en el IDE.
- Un IDE compatible con Java y Maven, como IntelliJ IDEA.
- Git, si deseas clonar el repositorio.

Puedes comprobar la instalación desde una terminal:

```bash
java -version
mvn -version
```

Asegúrate de que Java utilice una versión compatible con los requisitos del proyecto.

##  Instalación y ejecución

### 1. Obtener el proyecto

Si ya tienes el repositorio en tu equipo, abre su carpeta raíz. De lo contrario, clónalo utilizando la URL correspondiente:

```bash
git clone <URL_DEL_REPOSITORIO>
cd <CARPETA_DEL_PROYECTO>
```

Reemplaza los valores entre `< >` por la URL y el nombre reales del repositorio.

### 2. Compilar el proyecto

Desde la raíz del proyecto, ejecuta:

```bash
mvn compile
```

### 3. Ejecutar la aplicación principal

**Opción A: IntelliJ IDEA**

1. Abre el proyecto en IntelliJ IDEA.
2. Localiza `co.edu.uniquindio.app.Main`.
3. Haz clic derecho sobre la clase.
4. Selecciona **Run 'Main.main()'**.

**Opción B: Terminal**

Primero, compila el proyecto y después ejecuta la clase principal.

**Windows:**

```bash
mvn compile
java -cp "target/classes" co.edu.uniquindio.app.Main
```

**macOS / Linux:**

```bash
mvn compile
java -cp "target/classes" co.edu.uniquindio.app.Main
```

### 4. Ejecutar las pruebas

Para compilar las clases principales y las clases de prueba:

```bash
mvn compile test-compile
```

**Opción A: IntelliJ IDEA**

1. Localiza `co.edu.uniquindio.prueba.TodasLasPruebas`.
2. Haz clic derecho sobre la clase.
3. Selecciona **Run 'TodasLasPruebas.main()'**.

**Opción B: Terminal**

Una vez compiladas las clases, ejecuta el siguiente comando según tu sistema operativo.

**Windows:**

```bash
java -cp "target/classes;target/test-classes" co.edu.uniquindio.prueba.TodasLasPruebas
```

**macOS / Linux:**

```bash
java -cp "target/classes:target/test-classes" co.edu.uniquindio.prueba.TodasLasPruebas
```

> **Importante:** Como el proyecto utiliza un ejecutor de pruebas personalizado y no JUnit, `mvn test` no necesariamente ejecutará estas pruebas automáticamente. Para ejecutarlas, utiliza `TodasLasPruebas` como se indica anteriormente, salvo que se haya configurado Maven para invocarlas.

##  Estructuras de datos y algoritmos

El proyecto contempla el uso de los siguientes conceptos de Estructuras de Datos:

- **Listas simplemente enlazadas:** administración de colecciones mediante nodos.
- **Interfaces genéricas:** definición de operaciones reutilizables para diferentes tipos de datos.
- **Comparadores (`Comparator`):** establecimiento de criterios de ordenamiento para los paquetes.
- **Recursividad:** resolución de operaciones mediante llamadas recursivas.
- **Búsqueda binaria:** localización de elementos en colecciones previamente ordenadas.
- **Divide y vencerás (D&C):** resolución de problemas mediante su división en subproblemas.

La implementación concreta y el uso de cada algoritmo dependen de las clases y operaciones desarrolladas en el proyecto.

##  Reglas de dominio: prioridad de los paquetes

El rango de prioridad está pendiente de confirmación definitiva con el profesor, ya que el enunciado presenta referencias a dos escalas diferentes: de **0 a 10** y de **1 a 5**.

Mientras se confirma la regla oficial, el proyecto utiliza la escala de **1 a 5**, con la siguiente interpretación:

| Prioridad | Interpretación |
|---:|---|
| 1 | Prioridad más baja |
| 2 | Prioridad baja |
| 3 | Prioridad normal o predeterminada |
| 4 | Prioridad alta |
| 5 | Prioridad más alta |

Estos valores se gestionan mediante constantes definidas en la clase `Paquete`, con el objetivo de centralizar las reglas de prioridad