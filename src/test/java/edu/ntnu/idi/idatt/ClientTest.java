package edu.ntnu.idi.idatt;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    @Test
    public void testAddToFridgeDate() {
        //Best-før dato
        assertEquals(LocalDate.of(2003,3,21), testDate("21-03-2003"), "Expected Date of 21-03-2003, but got a different value.");

        assertThrows(RuntimeException.class, () -> testDate("11-02-20033\n"), "Expected an RuntimeException, but got something else.");
    }

    private LocalDate testDate(String input) {
        LocalDate date;
        try{
            String[] splitDateStr = input.split("-");
            int day = 0;
            int month = 0;
            int year = 0;
            for (String s : splitDateStr) {
                if (s.charAt(0) == '0') {
                    if (s.equals(splitDateStr[0])) {
                        String str = input.substring(0,input.indexOf('-'));
                        day = Integer.parseInt(String.join("", str.split("^0")));
                    }
                    else if (s.equals(splitDateStr[1])) {
                        String str = input.substring(input.indexOf('-')+1, input.indexOf('-',4));
                        month = Integer.parseInt(String.join("",str.split("^0")));
                    }
                }
                else {
                    if (s.equals(splitDateStr[0])) {
                        day = Integer.parseInt(s);
                    }
                    else if (s.equals(splitDateStr[1])) {
                        month = Integer.parseInt(s);
                    }
                    else {
                        year = Integer.parseInt(s);
                    }
                }
            }

            date = LocalDate.of(year,month,day);
            return date;
        }
        catch (Exception e) {
            throw new RuntimeException("Ugyldig format");
        }
    }
}