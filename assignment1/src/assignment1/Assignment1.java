/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

/**
 *
 * @author Zuha Ahmad 17777
 */
public class Assignment1 {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        String v = "dross";
        String w = "soars";
        int count = 0;

        for (int i = 1; i < v.length(); i++) {
            for (int j = 0; j < w.length(); j++) {
                if ((v.charAt(i) == w.charAt(j))) {
                    count++;
                    char[] x = w.toCharArray();
                    x[j] = '0';
                    w = String.valueOf(x);
                    break;
                }
            }
        }
        System.out.println(count);
        System.out.println(w);
        if (count == 4) {
            System.out.println("edge");
        }
        
    }
    
}
