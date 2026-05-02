package com.solvd.autoservicerepair.parsers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;

/**
 * XPath queries over garage.xml.
 *
 * WHAT IS XPath:
 *   XPath (XML Path Language) is a query language for XML, similar to SQL for databases.
 *   You write an expression like "/garage/mechanics/mechanic[level='SENIOR']"
 *   and it returns the matching nodes from the document.
 *
 * HOW IT WORKS IN JAVA:
 *   XPath in Java requires a DOM Document — it needs the full tree in memory
 *   to be able to navigate up, down, and sideways.
 *   That is why XPath and StAX are always separate: StAX is a stream reader
 *   (forward-only, no random access), XPath is a tree query language (random access).
 *   You CANNOT run XPath directly on a StAX stream.
 *
 * XPath SYNTAX CHEAT SHEET (used in this file):
 *   /garage/mechanics/mechanic     — path from root
 *   //mechanic                     — any <mechanic> anywhere in the document
 *   mechanic[level='SENIOR']       — predicate: mechanic whose <level> is SENIOR
 *   mechanic[1]                    — first mechanic (XPath indexes start at 1)
 *   text()                         — the text content of an element
 *   count(//mechanic)              — number of mechanic elements
 *   normalize-space(name)          — text content trimmed of whitespace
 *
 * RETURN TYPE CONSTANTS (XPathConstants):
 *   NODESET  — NodeList  — for queries that return multiple nodes
 *   STRING   — String    — for queries that return a single text value
 *   NUMBER   — Double    — for count(), sum() etc.
 *   BOOLEAN  — Boolean   — for exists checks
 */
public class GarageXPathQueries {

    private final Document document;
    private final XPath    xpath;

    /**
     * Constructor loads the XML file into a DOM Document.
     * @param xmlFilePath path to garage.xml
     */

    public GarageXPathQueries(String xmlFilePath) throws Exception {

        // DocumentBuilderFactory + DocumentBuilder = the standard Java DOM parser.
        // It loads the entire XML into memory as a tree of Node objects.

        document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new File(xmlFilePath));

        // XPathFactory creates the XPath engine.
        // XPath is the object you use to compile and evaluate expressions.
        xpath = XPathFactory.newInstance().newXPath();
    }

    // =========================================================================
    // QUERY METHODS
    // Each method shows one XPath pattern.
    // =========================================================================

    /**
     * Q1 — Get the garage name.
     *
     * XPath: /garage/name
     *   /garage  — start from the root element called "garage"
     *   /name    — go into its direct child called "name"
     *
     * XPathConstants.STRING returns a plain String
     * (the text content of the matched node).
     */
    public String getGarageName() throws Exception {
        return (String) xpath.evaluate(
                "/garage/name",
                document,
                XPathConstants.STRING);
    }

    /**
     * Q2 — Get all mechanic names.
     *
     * XPath: /garage/mechanics/mechanic/name
     *   Walks the full path from root to the <name> inside each mechanic.
     *   Returns a NodeList — one node per mechanic.
     */
    public NodeList getAllMechanicNames() throws Exception {
        return (NodeList) xpath.evaluate(
                "/garage/mechanics/mechanic/name",
                document,
                XPathConstants.NODESET);
    }

    /**
     * Q3 — Get all SENIOR or MASTER mechanics.
     *
     * XPath: //mechanic[level='SENIOR' or level='MASTER']
     *   //mechanic — finds any <mechanic> element anywhere in the document
     *   [level='SENIOR' or level='MASTER'] — predicate filter:
     *   keep only those where the child <level> equals one of these values
     */

    public NodeList getSeniorOrMasterMechanics() throws Exception {
        return (NodeList) xpath.evaluate(
                "//mechanic[level='SENIOR' or level='MASTER']",
                document,
                XPathConstants.NODESET);
    }

    /**
     * Q4 — Count total number of mechanics.
     *
     * XPath: count(//mechanic)
     *   count() is an XPath function that returns how many nodes match.
     *   Returns a Double (XPath numbers are always doubles).
     */

    public int getMechanicCount() throws Exception {
        Double count = (Double) xpath.evaluate(
                "count(//mechanic)",
                document,
                XPathConstants.NUMBER);
        return count.intValue();
    }

    /**
     * Q5 — Get a specific customer by idNumber.
     *
     * XPath: //customer[idNumber='19207150012']
     *   Predicate with a specific value — works like WHERE in SQL.
     */

    public Node getCustomerById(String idNumber) throws Exception {
        return (Node) xpath.evaluate(
                "//customer[idNumber='" + idNumber + "']",
                document,
                XPathConstants.NODE);
    }

    /**
     * Q6 — Get all customers with PREMIUM insurance.
     *
     * XPath: //customer[insurance/tier='PREMIUM']
     *   insurance/tier — navigate INTO the nested <insurance> child
     *   to read its <tier> child. Predicates can navigate into children.
     */

    public NodeList getCustomersWithPremiumInsurance() throws Exception {
        return (NodeList) xpath.evaluate(
                "//customer[insurance/tier='PREMIUM']",
                document,
                XPathConstants.NODESET);
    }

    /**
     * Q7 — Get all vehicles (car, motorcycle, truck).
     *
     * XPath: /garage/vehicles/*
     *   * (wildcard) matches any child element regardless of name.
     *   So this matches <car>, <motorcycle>, and <truck> all at once.
     */
    public NodeList getAllVehicles() throws Exception {
        return (NodeList) xpath.evaluate(
                "/garage/vehicles/*",
                document,
                XPathConstants.NODESET);
    }

    /**
     * Q8 — Get the truck specifically.
     *
     * XPath: /garage/vehicles/truck
     *   Direct path to the truck element.
     */

    public Node getTruck() throws Exception {
        return (Node) xpath.evaluate(
                "/garage/vehicles/truck",
                document,
                XPathConstants.NODE);
    }

    /**
     * Q9 — Check if the truck has a sleeping cabin.
     *
     * XPath: /garage/vehicles/truck/hasSleepingCabin
     *   Returns "true" or "false" as a String.
     *   This is the boolean field from the assignment.
     */

    public boolean truckHasSleepingCabin() throws Exception {
        String value = (String) xpath.evaluate(
                "/garage/vehicles/truck/hasSleepingCabin",
                document,
                XPathConstants.STRING);
        return Boolean.parseBoolean(value);
    }

    /**
     * Q10 — Get all appointments with status IN_PROGRESS.
     *
     * XPath: //appointment[status='IN_PROGRESS']
     */

    public NodeList getInProgressAppointments() throws Exception {
        return (NodeList) xpath.evaluate(
                "//appointment[status='IN_PROGRESS']",
                document,
                XPathConstants.NODESET);
    }

    /**
     * Q11 — Get the scheduledTime of appointment with id=1.
     *
     * XPath: //appointment[id='1']/scheduledTime
     *   After the predicate filter, /scheduledTime navigates into the child.
     *   This is the LocalDateTime field from the assignment (returned as String here).
     */

    public String getAppointmentScheduledTime(int appointmentId) throws Exception {
        return (String) xpath.evaluate(
                "//appointment[id='" + appointmentId + "']/scheduledTime",
                document,
                XPathConstants.STRING);
    }

    /**
     * Q12 — Get all spare parts with quantity greater than 0 (in stock).
     *
     * XPath: //sparePart[quantity > 0]
     */

    public NodeList getInStockSpareParts() throws Exception {
        return (NodeList) xpath.evaluate(
                "//sparePart[quantity > 0]",
                document,
                XPathConstants.NODESET);
    }

    /**
     * Q13 — Get the VIN of the vehicle referenced by appointment id=1.
     *
     * XPath: //appointment[id='1']/vehicleRef
     *   This returns the raw VIN string stored in <vehicleRef>.
     */

    public String getVehicleRefForAppointment(int appointmentId) throws Exception {
        return (String) xpath.evaluate(
                "//appointment[id='" + appointmentId + "']/vehicleRef",
                document,
                XPathConstants.STRING);
    }

    /**
     * Q14 — Check whether any MASTER mechanic exists.
     *
     * XPath: boolean(//mechanic[level='MASTER'])
     *   boolean() wraps the nodeset in a boolean — true if at least one node matched.
     */
    public boolean hasMasterMechanic() throws Exception {
        return (Boolean) xpath.evaluate(
                "boolean(//mechanic[level='MASTER'])",
                document,
                XPathConstants.BOOLEAN);
    }

    /**
     * Q15 — Get all customers whose insurance has already expired.
     *
     * XPath: //customer[insurance/expiryDate < '2026-04-25']
     *   Date strings in ISO format sort lexicographically correctly,
     *   so string comparison works as date comparison for xs:date values.
     *   Replace the hardcoded date with today's date in real code.
     */
    public NodeList getCustomersWithExpiredInsurance(String today) throws Exception {
        return (NodeList) xpath.evaluate(
                "//customer[insurance/expiryDate < '" + today + "']",
                document,
                XPathConstants.NODESET);
    }

    // =========================================================================
    // Print a NodeList to stdout
    // =========================================================================

    /**
     * Prints each node's text content from a NodeList.
     * Useful for quickly verifying query results in demos.
     *
     * @param label    description printed before the list
     * @param nodeList the result of an XPath NODESET query
     */

    public static void printNodeList(String label, NodeList nodeList) {
        System.out.println("\n--- " + label + " ---");
        if (nodeList.getLength() == 0) {
            System.out.println("  (no results)");
            return;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            // getTextContent() returns all text inside the element, including children.
            System.out.println("  [" + (i + 1) + "] " + node.getNodeName()
                    + ": " + node.getTextContent().trim().replaceAll("\\s+", " "));
        }
    }

    // =========================================================================
    // Demo application: runs all XPath queries on garage.xml
    // =========================================================================

    public static void main(String[] args) throws Exception {

        // Adjust the path to wherever your garage.xml lives.
        GarageXPathQueries q = new GarageXPathQueries(
                "src/main/resources/garage.xml");

        // Q1
        System.out.println("Garage name: " + q.getGarageName());

        // Q2
        printNodeList("All mechanic names", q.getAllMechanicNames());

        // Q3
        printNodeList("SENIOR or MASTER mechanics", q.getSeniorOrMasterMechanics());

        // Q4
        System.out.println("\nTotal mechanics: " + q.getMechanicCount());

        // Q5
        Node customer = q.getCustomerById("19207150012");
        System.out.println("\nCustomer 19207150012: " +
                (customer != null ? customer.getTextContent().trim().replaceAll("\\s+", " ") : "not found"));

        // Q6
        printNodeList("PREMIUM insurance customers", q.getCustomersWithPremiumInsurance());

        // Q7
        printNodeList("All vehicles", q.getAllVehicles());

        // Q8
        Node truck = q.getTruck();
        System.out.println("\nTruck: " +
                (truck != null ? truck.getTextContent().trim().replaceAll("\\s+", " ") : "not found"));

        // Q9
        System.out.println("\nTruck has sleeping cabin: " + q.truckHasSleepingCabin());

        // Q10
        printNodeList("IN_PROGRESS appointments", q.getInProgressAppointments());

        // Q11
        System.out.println("\nAppointment 1 scheduled time: " +
                q.getAppointmentScheduledTime(1));

        // Q12
        printNodeList("In-stock spare parts (qty > 0)", q.getInStockSpareParts());

        // Q13
        System.out.println("\nVehicle ref for appointment 1: " +
                q.getVehicleRefForAppointment(1));

        // Q14
        System.out.println("\nHas master mechanic: " + q.hasMasterMechanic());

        // Q15 — today's date
        printNodeList("Customers with expired insurance",
                q.getCustomersWithExpiredInsurance("2026-04-25"));
    }
}
