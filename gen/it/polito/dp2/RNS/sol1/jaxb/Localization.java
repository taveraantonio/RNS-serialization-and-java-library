//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.13 at 09:15:05 PM CET 
//


package it.polito.dp2.RNS.sol1.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for localization complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="localization">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="comesFrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="directedTo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="position" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "localization", propOrder = {
    "comesFrom",
    "directedTo",
    "position"
})
public class Localization {

    @XmlElement(required = true)
    protected String comesFrom;
    @XmlElement(required = true)
    protected String directedTo;
    @XmlElement(required = true)
    protected String position;

    /**
     * Gets the value of the comesFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComesFrom() {
        return comesFrom;
    }

    /**
     * Sets the value of the comesFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComesFrom(String value) {
        this.comesFrom = value;
    }

    /**
     * Gets the value of the directedTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectedTo() {
        return directedTo;
    }

    /**
     * Sets the value of the directedTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectedTo(String value) {
        this.directedTo = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosition(String value) {
        this.position = value;
    }

}
