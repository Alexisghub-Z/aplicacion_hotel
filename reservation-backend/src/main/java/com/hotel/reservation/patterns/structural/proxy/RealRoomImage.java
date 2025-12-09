package com.hotel.reservation.patterns.structural.proxy;

/**
 * PATRÓN PROXY - Real Subject
 *
 * Objeto real que carga y almacena la imagen
 * Costoso de crear debido a la carga de datos
 */
public class RealRoomImage implements RoomImage {

    private final String url;
    private byte[] imageData;
    private final long size;

    public RealRoomImage(String url) {
        this.url = url;
        // Simular carga costosa de imagen
        loadImageFromDisk();
        this.size = imageData != null ? imageData.length : 0;
    }

    /**
     * Simula la carga de imagen desde disco o red (operación costosa)
     */
    private void loadImageFromDisk() {
        System.out.println("Cargando imagen desde: " + url);
        try {
            // Simular tiempo de carga
            Thread.sleep(1000);

            // Simular datos de imagen (en producción sería la imagen real)
            this.imageData = new byte[1024 * 500]; // 500KB simulado

            System.out.println("✓ Imagen cargada: " + url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error cargando imagen: " + e.getMessage());
        }
    }

    @Override
    public void display() {
        System.out.println("Mostrando imagen: " + url);
        // En producción, aquí se renderizaría la imagen
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public long getSize() {
        return size;
    }

    /**
     * Obtiene los datos de la imagen
     * @return datos binarios
     */
    public byte[] getImageData() {
        return imageData;
    }
}
