package com.hotel.reservation.patterns.structural.proxy;

/**
 * PATRÓN PROXY - Proxy
 *
 * Propósito: Controla el acceso a RealRoomImage, cargando la imagen
 * solo cuando es realmente necesaria (lazy loading).
 *
 * Beneficios:
 * - Carga diferida (lazy loading) - no carga hasta que se necesita
 * - Ahorro de memoria - solo carga imágenes que se usan
 * - Mejora rendimiento inicial - no carga todas las imágenes al inicio
 * - Control de acceso - puede agregar validaciones, logs, etc.
 *
 * Tipos de Proxy implementados aquí:
 * - Virtual Proxy: controla la creación de objetos costosos
 * - Protection Proxy: puede verificar permisos antes de cargar
 *
 * Ejemplo de uso:
 * RoomImage image = new ImageProxy("/images/room101.jpg");
 * // La imagen NO se ha cargado todavía
 *
 * image.display(); // AHORA se carga la imagen
 */
public class ImageProxy implements RoomImage {

    private final String url;
    private RealRoomImage realImage;
    private boolean isLoaded;

    public ImageProxy(String url) {
        this.url = url;
        this.isLoaded = false;
        System.out.println("ImageProxy creado para: " + url + " (imagen NO cargada todavía)");
    }

    /**
     * Carga la imagen real solo si aún no está cargada
     * (Lazy initialization)
     */
    private void loadImage() {
        if (!isLoaded) {
            realImage = new RealRoomImage(url);
            isLoaded = true;
        }
    }

    @Override
    public void display() {
        // Carga la imagen solo cuando se necesita mostrar
        loadImage();
        realImage.display();
    }

    @Override
    public String getUrl() {
        // El URL se puede obtener sin cargar la imagen
        return url;
    }

    @Override
    public long getSize() {
        // Para obtener el tamaño, necesitamos cargar la imagen
        if (!isLoaded) {
            loadImage();
        }
        return realImage.getSize();
    }

    /**
     * Verifica si la imagen ha sido cargada
     * @return true si está cargada
     */
    public boolean isImageLoaded() {
        return isLoaded;
    }

    /**
     * Pre-carga la imagen (útil para pre-caching)
     */
    public void preload() {
        loadImage();
    }

    /**
     * Libera la imagen de memoria (para gestión de recursos)
     */
    public void unload() {
        if (isLoaded) {
            realImage = null;
            isLoaded = false;
            System.out.println("Imagen liberada de memoria: " + url);
        }
    }
}
