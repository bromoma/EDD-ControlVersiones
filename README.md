# Calculadora Java

Actividad evaluable de Control de Versiones de Entornos de Desarrollo.

Aplicacion hecha con Java y HTML que solo hace estas operaciones:

- suma
- resta
- multiplicacion
- potencia

Los dos numeros tienen que ser enteros de 5 cifras como maximo, es decir, entre -99999 y 99999. Si no, la calculadora muestra un error.

## Como ejecutarla

Hace falta Java 11 o superior. Desde la carpeta del proyecto:

```bash
java Calculadora.java
```

Despues se abre en el navegador http://localhost:8080

## Ramas

| Rama | Que contiene |
|---|---|
| `main` | El primer commit con el README, los merges de las otras ramas y los commits que solo cambian comentarios |
| `backend` | Solo commits de archivos `.java` |
| `frontend` | Solo commits de archivos `.html` |

## Comandos usados (en orden)

### 1. Crear el repositorio y publicarlo en GitHub

```bash
mkdir EDD-ControlVersiones
cd EDD-ControlVersiones
git init -b main
```

- `mkdir` crea la carpeta del proyecto y `cd` entra en ella.
- `git init -b main` convierte la carpeta en un repositorio de Git. Con `-b main` la rama principal se llama `main` y no `master`.

Despues cree el archivo `README.md` con el titulo del proyecto.

```bash
git add README.md
git commit -m "Primer commit con el README"
```

- `git add` pasa el archivo al area de preparacion (staging). Es decir, le dice a Git que este archivo va a entrar en el siguiente commit.
- `git commit -m "..."` guarda una "foto" de los archivos preparados con un mensaje que explica el cambio.

```bash
gh repo create EDD-ControlVersiones --public --source=. --remote=origin --push
```

- `gh` es la herramienta oficial de GitHub para la terminal.
- Este comando crea el repositorio publico en GitHub, lo conecta con la carpeta como remoto `origin` y sube la rama `main`.
- Hace lo mismo que crear el repositorio en la web y luego usar `git remote add` y `git push` (se explica abajo, en "Como publicar un repositorio en GitHub").

```bash
git log --oneline
git remote -v
```

- `git log --oneline` muestra la lista de commits, uno por linea.
- `git remote -v` muestra a que direccion de GitHub esta conectado el repositorio.

### 2. Rama backend (Java)

```bash
git checkout -b backend
```

- Crea la rama `backend` y se cambia a ella. Todo lo que haga ahora no afecta a `main`.

Cree `Calculadora.java`: un servidor web con las cuatro operaciones.

```bash
git add Calculadora.java
git commit -m "Añadir la calculadora en Java"
```

Despues añadi la comprobacion de que los numeros son enteros de 5 cifras como maximo.

```bash
git status --short
git add Calculadora.java
git commit -m "Comprobar que los numeros sean enteros de 5 cifras"
git push -u origin backend
```

- `git status --short` muestra que archivos han cambiado (`M` = modificado).
- `git push -u origin backend` sube la rama a GitHub. Con `-u` Git recuerda la rama remota y la proxima vez basta con `git push`.

### 3. Rama frontend (HTML)

```bash
git checkout main
git checkout -b frontend
```

- Primero vuelvo a `main` para que la rama `frontend` empiece desde ahi y no desde `backend`.

Cree `index.html` con el formulario.

```bash
git add index.html
git commit -m "Añadir el formulario de la calculadora"
git log --oneline
```

Despues añadi los estilos CSS dentro del mismo `index.html`.

```bash
git add index.html
git commit -m "Dar estilo a la calculadora"
git push -u origin frontend
```

### 4. Unir las ramas en main

```bash
git checkout main
git merge --no-ff backend -m "Merge branch 'backend'"
git merge --no-ff frontend -m "Merge branch 'frontend'"
git log --oneline --graph
git push origin main
```

- `git merge` trae los commits de otra rama a la rama actual (`main`).
- `--no-ff` obliga a crear un commit de merge. Asi en el historial se ve claramente cuando se unio cada rama.
- `git log --oneline --graph` dibuja las ramas y los merges en la terminal.
- `git push origin main` sube `main` con los merges a GitHub.

Para probar la calculadora:

```bash
java Calculadora.java
```

### 5. Comentarios en main

En `main` solo se pueden hacer commits de comentarios, asi que añadi los comentarios del codigo directamente aqui.

```bash
git status --short
git diff --stat
git diff
git add Calculadora.java index.html
git commit -m "Añadir comentarios al codigo"
```

- `git diff --stat` dice cuantas lineas cambian en cada archivo.
- `git diff` enseña los cambios linea por linea. Lo use para comprobar que solo habia comentarios nuevos.

### 6. Este README

```bash
git add README.md
git commit -m "Explicar los comandos en el README"
git push origin main
```

## Como publicar un repositorio en GitHub

Hay dos formas.

**A) Empezar en GitHub (recomendado, con README ya creado)**

1. En github.com: boton **New**, poner el nombre, marcar **Public** y **Add a README file**, y **Create repository**.
2. Copiar la direccion del boton verde **Code**.
3. En la terminal:

```bash
git clone https://github.com/usuario/repositorio.git
cd repositorio
```

`git clone` descarga el repositorio y deja el remoto `origin` ya configurado. Despues se trabaja con `git add`, `git commit` y `git push`.

**B) Empezar en el ordenador (como en este proyecto)**

1. En github.com crear un repositorio **vacio** (sin README).
2. En la carpeta del proyecto:

```bash
git init -b main
git add .
git commit -m "Primer commit"
git remote add origin https://github.com/usuario/repositorio.git
git push -u origin main
```

- `git remote add origin URL` conecta la carpeta con el repositorio de GitHub.
- `git push -u origin main` sube la rama `main` por primera vez.

Con la herramienta `gh` los pasos 1 y 2 se pueden hacer con un solo comando: `gh repo create nombre --public --source=. --push`.
