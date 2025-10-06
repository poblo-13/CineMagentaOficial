package model;

import java.time.Year;

public class Pelicula {
    private int id;
    private String titulo;
    private String director;
    private int anio;
    private int duracion;
    private String genero;
    
    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El Título de la película no puede estar vacío.");
        }
        if (titulo.length() > 150) {
            throw new IllegalArgumentException("El Título excede los 150 caracteres permitidos.");
        }
    }
    
    private void validarDirector(String director) {
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("El Director de la película no puede estar vacío.");
        }
        if (director.length() > 50) {
            throw new IllegalArgumentException("El Director excede los 50 caracteres permitidos.");
        }
    }
    
    private void validarGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("El Género de la película no puede estar vacío.");
        }
    }
    
    private void validarAnio(int anio) {
        int anioActual = Year.now().getValue();
        if (anio < 1888 || anio > anioActual) {
            throw new IllegalArgumentException("El Año (" + anio + ") debe ser una fecha histórica de estreno válida (1888 - " + anioActual + ").");
        }
    }
    
    private void validarDuracion(int duracion) {
        if (duracion <= 0 || duracion > 400) { 
            throw new IllegalArgumentException("La Duración (" + duracion + " min) debe ser un valor positivo y menor a 400.");
        }
    }

    // constructores
    
    public Pelicula(int id, String titulo, String director, int anio, int duracion, String genero) {
        setId(id);
        setTitulo(titulo);
        setDirector(director);
        setAnio(anio);
        setDuracion(duracion);
        setGenero(genero);
    }

    public Pelicula(String titulo, String director, int anio, int duracion, String genero) {
        this(0, titulo, director, anio, duracion, genero);
    }

    // getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDirector() { return director; }
    public int getAnio() { return anio; }
    public int getDuracion() { return duracion; }
    public String getGenero() { return genero; }

    // setters 
    public void setId(int id) { 
        if (id < 0) throw new IllegalArgumentException("El ID de la película no puede ser negativo.");
        this.id = id; 
    }
    
    public void setTitulo(String titulo) {
        validarTitulo(titulo);
        this.titulo = titulo.trim();
    }
    
    public void setDirector(String director) {
        validarDirector(director);
        this.director = director.trim();
    }
    
    public void setAnio(int anio) {    
        validarAnio(anio);
        this.anio = anio;
    }
    
    public void setDuracion(int duracion) {    
        validarDuracion(duracion);
        this.duracion = duracion;
    }
    
    public void setGenero(String genero) {    
        validarGenero(genero);
        this.genero = genero.trim();
    }
}