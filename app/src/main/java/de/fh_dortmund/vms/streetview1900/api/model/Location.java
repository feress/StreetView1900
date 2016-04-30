package de.fh_dortmund.vms.streetview1900.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {

    private int id;
    private double latitude;
    private double longitude;
    private String name;
    private String description;
    private Category category;
    private List<ImageInformation> imageInformation = new ArrayList<>();
    private String dateOfConstruction;
    private String dateOfDestruction;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category The category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return The imageInformation
     */
    public List<ImageInformation> getImageInformation() {
        return imageInformation;
    }

    /**
     * @param imageInformation The imageInformation
     */
    public void setImageInformation(List<ImageInformation> imageInformation) {
        this.imageInformation = imageInformation;
    }

    /**
     * @return The dateOfConstruction
     */
    public String getDateOfConstruction() {
        return dateOfConstruction;
    }

    /**
     * @param dateOfConstruction The dateOfConstruction
     */
    public void setDateOfConstruction(String dateOfConstruction) {
        this.dateOfConstruction = dateOfConstruction;
    }

    /**
     * @return The dateOfDestruction
     */
    public String getDateOfDestruction() {
        return dateOfDestruction;
    }

    /**
     * @param dateOfDestruction The dateOfDestruction
     */
    public void setDateOfDestruction(String dateOfDestruction) {
        this.dateOfDestruction = dateOfDestruction;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", imageInformation=" + imageInformation +
                ", dateOfConstruction='" + dateOfConstruction + '\'' +
                ", dateOfDestruction='" + dateOfDestruction + '\'' +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}