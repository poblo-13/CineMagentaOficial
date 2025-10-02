package model;

import java.time.Year;

public class Pelicula {
    private int id;
    private String titulo;
    private String director;
    private int anio;
    private int duracion;
    private String genero;
    
    // metodo privado para aplicar todas las validaciones
    private void validarDatos(String titulo, String director, int anio, int duracion, String genero) {
        // validacion 1 titulo y Director no pueden ser nulos
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El Título de la película no puede estar vacío.");
        }
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("El Director de la película no puede estar vacío.");
        }
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("El Género de la película no puede estar vacío.");
        }
        
        // validacion 2 año y duracion dentro de rangos logicos
        int anioActual = Year.now().getValue();
        if (anio < 1888 || anio > anioActual) {
            throw new IllegalArgumentException("El Año (" + anio + ") debe ser una fecha histórica de estreno válida (1888 - " + anioActual + ").");
        }
        
        if (duracion <= 0 || duracion > 400) { // rango logico de duracion en minutos
            throw new IllegalArgumentException("La Duración (" + duracion + " min) debe ser un valor positivo y menor a 400.");
        }
        
        // validacion 3 longitud
        if (titulo.length() > 150) {
            throw new IllegalArgumentException("El Título excede los 150 caracteres permitidos.");
        }
        if (director.length() > 50) {
            throw new IllegalArgumentException("El Director excede los 50 caracteres permitidos.");
        }
    }

    public Pelicula(int id, String titulo, String director, int anio, int duracion, String genero) {
        validarDatos(titulo, director, anio, duracion, genero);
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.anio = anio;
        this.duracion = duracion;
        this.genero = genero;
    }

    // constructor para crear una nueva pelicula sin ID, el cual sera creado por la BD
    public Pelicula(String titulo, String director, int anio, int duracion, String genero) {
        this(0, titulo, director, anio, duracion, genero);
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDirector() { return director; }
    public int getAnio() { return anio; }
    public int getDuracion() { return duracion; }
    public String getGenero() { return genero; }

    // Setters
    public void setId(int id) { this.id = id; } 
    
    public void setTitulo(String titulo) {
        validarDatos(titulo, this.director, this.anio, this.duracion, this.genero);
        this.titulo = titulo;
    }
    
    public void setDirector(String director) {
        validarDatos(this.titulo, director, this.anio, this.duracion, this.genero);
        this.director = director;
    }
    
    public void setAnio(int anio) { 
        validarDatos(this.titulo, this.director, anio, this.duracion, this.genero);
        this.anio = anio;
    }
    
    public void setDuracion(int duracion) { 
        validarDatos(this.titulo, this.director, this.anio, duracion, this.genero);
        this.duracion = duracion;
    }
    
    public void setGenero(String genero) { 
        validarDatos(this.titulo, this.director, this.anio, this.duracion, genero);
        this.genero = genero;
    }
}