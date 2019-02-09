//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.01.13 at 09:15:05 PM CET 
//


package it.polito.dp2.RNS.sol1.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for roadSegment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="roadSegment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="road">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="roadName" use="required" type="{}alphaNumericString" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="roadSegmentName" use="required" type="{}alphaNumericString" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roadSegment", propOrder = {
    "road"
})
public class RoadSegment {

    @XmlElement(required = true)
    protected RoadSegment.Road road;
    @XmlAttribute(name = "roadSegmentName", required = true)
    protected String roadSegmentName;

    /**
     * Gets the value of the road property.
     * 
     * @return
     *     possible object is
     *     {@link RoadSegment.Road }
     *     
     */
    public RoadSegment.Road getRoad() {
        return road;
    }

    /**
     * Sets the value of the road property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoadSegment.Road }
     *     
     */
    public void setRoad(RoadSegment.Road value) {
        this.road = value;
    }

    /**
     * Gets the value of the roadSegmentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoadSegmentName() {
        return roadSegmentName;
    }

    /**
     * Sets the value of the roadSegmentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoadSegmentName(String value) {
        this.roadSegmentName = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="roadName" use="required" type="{}alphaNumericString" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Road {

        @XmlAttribute(name = "roadName", required = true)
        protected String roadName;

        /**
         * Gets the value of the roadName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRoadName() {
            return roadName;
        }

        /**
         * Sets the value of the roadName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRoadName(String value) {
            this.roadName = value;
        }

    }

}