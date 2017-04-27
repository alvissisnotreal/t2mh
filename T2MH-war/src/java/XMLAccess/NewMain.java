/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLAccess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class NewMain {

    private String[][] lock = 
                                //0
    {                       {"", " "},
    //      1                   2                             3
    {},                 {"","A", "B", "C"},             {"","D", "E", "F"},
    //      4                   5                               6
    {"","G", "H", "I"}, {"","J", "K", "L"},             {"","M", "N", "O"},
    //      7                   8                               9
    {"","P", "Q", "R", "S"}, {"","T", "U", "V"}, {"","W", "X", "Y", "Z"}};
    private List<int[]> key = new ArrayList<int[]>();

    private void autoGenerate() {
        key.add(new int[]{6, 1});
        key.add(new int[]{6, 3});
        key.add(new int[]{2, 2});
        key.add(new int[]{4, 3});
        key.add(new int[]{5, 3});
        key.add(new int[]{3, 2});
        key.add(new int[]{0, 1});
        key.add(new int[]{3, 1});
        key.add(new int[]{2, 1});
        key.add(new int[]{9, 3});
        
    }

    public NewMain() {
        autoGenerate();
        for (Iterator<int[]> iterator = key.iterator(); iterator.hasNext();) {
            int[] next = iterator.next();
            System.out.print(lock[next[0]][next[1]]);
        }
        System.out.println("\t\t");
    }

    public static void main(String[] args) {
        NewMain nm=new NewMain();
    }

}
