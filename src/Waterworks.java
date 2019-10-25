/**
 * Waterworks administration project
 *
 * @author Jens Christensen, Kasper S. Dinsen and Omid Rezaei
 * @version 1.0 Build 2019-10-25
 */

import java.time.LocalDate;
import java.util.Scanner;

public class Waterworks
{
    /**
     * Main function entrypoint
     * @param args
     */
    public static void main(String[] args)
    {
        // Menu display options
        String[] menu = {"MENU", "1. Customer management", "2. Statistic", "3. Update meter", "4. Manage property", "5. Quit"};

        // Program state indicator(true/false)
        boolean run = true;

        // Run program until state is false
        while (run)
        {
            // Display all menu options
            printStringArray(menu);

            // Get int input between the numbers of menu options
            int menuChoice =  numberBetween(1, menu.length - 1) - 1;

            // Call menu option function
            switch (menuChoice)
            {
                case 0:
                    customerMenu();
                    break;
                case 2:
                    updateMeter();
                    break;
                case 3:
                    propertyMenu();
                    break;
                case 4:
                    run = false;
                    break;
            }
        }
    }

    /**
     * Helper function takes a String array and print them out to the console
     * @param array
     */
    public static void printStringArray(String[] array)
    {
        // Loop though array and print each element
        for (String element: array)
        {
            System.out.println(element);
        }
    }

    /**
     * Verifies that the input number is between two given numbers
     * @param first from the number
     * @param last to the number
     * @return the number between fist and last
     */
    public static int numberBetween(int first, int last)
    {
        // Declare int input variable
        int input;

        // Get input int until it is between first and last
        do
        {
            input = intInput();
        }
        while (input < first || input > last);

        return input;
    }

    /**
     * Helper function to verify that given input is an int
     * @return verified int input
     */
    public static int intInput()
    {
        Scanner scn = new Scanner(System.in);

        while (!scn.hasNextInt())
        {
            scn.next();
        }

        return scn.nextInt();
    }

    /**
     * Helper function to verify that a given input is and String
     * @return verified String input
     */
    public static String stringInput()
    {
        Scanner in = new Scanner(System.in);

        while (!in.hasNext())
        {
            in.nextLine();
        }

        return in.nextLine();
    }

    /**
     * Helper function to add text output via field parameter to the stringInput() helper function
     * @param field
     * @return verified String input
     */
    public static String stringInputText(String field)
    {
        System.out.print(field);

        return stringInput();
    }

    /**
     * Show menu with all customer related tasks
     */
    public static void customerMenu()
    {
        String[] menu = {"Customer menu", "1. Create new customer", "2. Update customer info", "3. Delete customer", "4. Show customer info", "5. Back"};

        printStringArray(menu);

        int menuChoice =  numberBetween(1, menu.length - 1) - 1;

        switch (menuChoice)
        {
            case 0:
                createCustomer();
                break;
            case 1:
                updateCustomerInfo();
                break;
            case 2:
                deleteCustomer();
                break;
            case 3:
                showCustomer();
                break;
            case 4:
                break;
        }
    }

    /**
     * Ask customer information and create new customer
     */
    public static void createCustomer()
    {
        Scanner in = new Scanner(System.in);
        String name, phone, cprNo, birthday; // Variables declaration to hold scanner values

        name = stringInputText("Please write customer name");
        phone = stringInputText("Please write customer phone number");
        cprNo = stringInputText("Please write customer CPR number");
        birthday = stringInputText("Please write customer birthday as follows year-month-day");

        DB.insertSQL("INSERT INTO tblCustomer (fldName, fldPhone, fldCPRNo, fldBirthday) VALUES('"+name+"','" + phone +"', '" + cprNo + "', '"+ birthday + "')");
    }

    /**
     * Ask for customer number and show information
     * @return customer number
     */
    public static int showCustomer()
    {
        System.out.println("Please write a customer number");
        int customerNumber = intInput();

        DB.selectSQL("SELECT * FROM tblCustomer where fldCustomerID ="+customerNumber+" ");

        System.out.println("Customer info");
        System.out.println("ID - Name - PhoneNo - CPR - Birthday");

        do
        {
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA))
            {
                break;
            }
            else
            {
                System.out.print(data);
            }
        } while(true);

        return customerNumber;
    }

    /**
     * Show customer and ask for that to update
     */
    public static void updateCustomerInfo()
    {
        int customerNumber = showCustomer();

        System.out.println("What would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. PhoneNo");

        int updateChoice = intInput();

        if (updateChoice == 1)
        {
            System.out.println("Please write a new name");
            DB.updateSQL("UPDATE tblCustomer SET fldName='" + stringInput()+"' WHERE fldCustomerID=" + customerNumber+ "");
            System.out.println("name is updated successfully");
        }
        else if (updateChoice == 2)
        {
            System.out.println("Please write a new phone number");
            DB.updateSQL("UPDATE tblCustomer SET fldPhone='" + stringInput()+"' WHERE fldCustomerID=" + customerNumber+ "");
            System.out.println("Phone is updated successfully");
        }
    }

    /**
     * Show customer and ask to delete
     */
    public static void deleteCustomer()
    {
        int customerNumber = showCustomer();

        System.out.println("Are you sure you want to delete this customer?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int updateChoice = intInput();

        if(updateChoice == 1)
        {
            DB.deleteSQL("DELETE FROM tblCustomer WHERE fldCustomerID=" + customerNumber+ "");
            System.out.println("The customer is deletede successfully");
        }
        else if (updateChoice == 2)
        {
            System.out.println("Customer is not deleted");
        }
    }

    /**
     * Get meter id and add new reading
     */
    public static void updateMeter()
    {
        LocalDate date = LocalDate.now();
        System.out.println("Update meter consumption from reading card");
        System.out.println("Please write a meter ID");
        int meterID = intInput();
        System.out.println("Please write the volume in kubicmetre");
        float volume = intInput();

        DB.insertSQL("INSERT INTO tblConsumption (fldMeterID, fldVolume, fldDate) VALUES("+meterID+","+ volume +",'"+ date +"')");
        System.out.println("The consumption was inserted successfully for meterID: " + meterID);
    }

    /**
     * Show property and segment menu
     */
    public static void propertyMenu()
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Property menu");
        System.out.println("1. Property management");
        System.out.println("2. Customer segment management");

        int choice = in.nextInt();

        if (choice == 1)
        {
            propertyManagementMenu();
        }
        else if (choice == 2)
        {
            segmentMenu();
        }
    }

    /**
     * Show propertyManagement menu options and ask for choice
     */
    public static void propertyManagementMenu()
    {
        String[] menu = {"Property menu", "1. Create new property", "2. Update property info", "3. Delete property", "4. Show property info", "5. Create meter"};

        printStringArray(menu);

        int menuChoice = numberBetween(1, menu.length - 1) - 1;

        switch (menuChoice) {
            case 0:
                createProperty();
                break;
            case 1:
                updateProperty();
                break;
            case 2:
                deleteProperty();
                break;
            case 3:
                showProperty();
                break;
            case 4:
                createMeter();
                break;
        }
    }

    /**
     * Ask for property data and create new property
     */
    public static void createProperty()
    {
        Scanner in = new Scanner(System.in);
        String zip, customerID, segmentID, address; // Variables declaration to hold scanner values

        zip = stringInputText("Please write property zip code");
        customerID = stringInputText("Please write customerID of owner of the property");
        segmentID = stringInputText("Please write property segmentID");
        address = stringInputText("Please write the address of the customer as adress");

        DB.insertSQL("INSERT INTO tblProperty (fldZip, fldCustomerID, fldSegmentID, fldAddress) VALUES('"+zip+"'," + customerID+", " + segmentID + ", '"+ address+ "')");

        System.out.println("Property was created successfully");
    }

    /**
     * Show property information and update selected data
     */
    public static void updateProperty()
    {
        int propertyNumber = showProperty();

        System.out.println("What would you like to update?");
        System.out.println("1. CustomerID");
        System.out.println("2. SegmentID");

        int updateChoice = intInput();

        if (updateChoice == 1)
        {
            System.out.println("Please write a new customerID");
            DB.updateSQL("UPDATE tblProperty SET fldCustomerID=" + intInput()+" WHERE fldPropertyID=" + propertyNumber+ "");
            System.out.println("CustomerID is updated successfully");
        }
        else if (updateChoice == 2)
        {
            System.out.println("Please write a new segmentID");
            DB.updateSQL("UPDATE tblProperty SET fldSegmentID=" + intInput()+" WHERE fldPropertyID=" + propertyNumber+ "");
            System.out.println("Segment ID is updated successfully");
        }
    }

    /**
     * Show property and ask to delete
     */
    public static void deleteProperty()
    {
        int propertyNumber = showProperty();

        System.out.println("Are you sure you want to delete this property?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int deleteChoice = intInput();

        if (deleteChoice == 1)
        {
            DB.deleteSQL("DELETE FROM tblProperty WHERE fldPropertyID=" + propertyNumber+ "");
            System.out.println("The property is deleted successfully");
        }
        else if (deleteChoice == 2)
        {
            System.out.println("The property was not deleted");
        }
    }

    /**
     * Show property and return property number
     * @return propertyNumber
     */
    public static int showProperty()
    {
        System.out.println("Please write a property ID");
        int propertyNumber = intInput();

        DB.selectSQL("SELECT * FROM tblProperty where fldPropertyID ="+propertyNumber+" ");

        System.out.println("Property info");
        System.out.println("ID - Zip - CustomerID - SegmentID - Address");

        do
        {
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA))
            {
                break;
            }
            else
            {
                System.out.print(data);
            }
        } while(true);

        return propertyNumber;
    }

    /**
     * Add new meter to property
     */
    public static void createMeter()
    {
        int propertyID;

        System.out.print("Please write a property number: ");
        propertyID = intInput();
        DB.insertSQL("INSERT INTO tblMeter (fldPropertyID) VALUES("+propertyID+")");

        System.out.println("Property was created successfully");
    }

    /**
     * Show segment menu options
     */
    public static void segmentMenu()
    {
        String[] menu = {"Customer segment menu", "1. Create new segment", "2. Update segment info", "3. Delete segment", "4. Show segment info"};

        printStringArray(menu);

        int menuChoice = numberBetween(1, menu.length - 1) - 1;

        switch (menuChoice) {
            case 0:
                createSegment();
                break;
            case 1:
                updateSegment();
                break;
            case 2:
                deleteSegment();
                break;
        }
    }

    /**
     * Show segment information
     * @return
     */
    public static int showSegment()
    {
        System.out.println("Please write a segment number");
        int segmentNumber = intInput();

        DB.selectSQL("SELECT * FROM tblSegment where fldSegmentID ="+segmentNumber+" ");

        System.out.println("Segment info");
        System.out.println("ID - Name - Taxrate");

        do
        {
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA))
            {
                break;
            }
            else
            {
                System.out.print(data);
            }
        } while(true);

        return  segmentNumber;
    }

    /**
     * Create new segment
     */
    public static void createSegment()
    {
        // Variables declaration to hold values
        String name, taxrate;

        name = stringInputText("Please write segment name");
        taxrate = stringInputText("Please write the taxrate of the segment");

        DB.insertSQL("INSERT INTO tblSegment (fldName, fldTaxrate) VALUES('"+name+"'," + taxrate+")");

        System.out.println("Segment was created successfully");
    }

    /**
     * Show segment and update selected information
     */
    public static void updateSegment()
    {
        int segmentNumber = showSegment();

        System.out.println("What would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Taxrate");

        int updateChoice = intInput();

        if (updateChoice == 1)
        {
            System.out.println("Please write a new segment name");
            DB.updateSQL("UPDATE tblSegment SET fldName=" + intInput()+" WHERE fldSegmentID=" + segmentNumber+ "");
            System.out.println("Segment name is updated successfully");
        }
        else if (updateChoice == 2)
        {
            System.out.println("Please write a new taxrate");
            DB.updateSQL("UPDATE tblSegment SET fldTaxrate=" + intInput()+" WHERE fldSegmentID=" + segmentNumber+ "");
            System.out.println("Taxrate is updated successfully");
        }
    }

    /**
     * Show segment information and ask to delete
     */
    public static void deleteSegment()
    {
        int segmentNumber = showSegment();

        System.out.println("Are you sure you want to delete this segment?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int deleteChoice = intInput();

        if (deleteChoice == 1)
        {
            DB.deleteSQL("DELETE FROM tblSegment WHERE fldSegmentID=" + segmentNumber+ "");
            System.out.println("The segment is deleted successfully");
        }
        else if (deleteChoice == 2)
        {
            System.out.println("The segment was not deleted");
        }
    }
}