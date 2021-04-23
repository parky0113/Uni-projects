/**
 * Equation represents a Bydysawd chemical equation. 
 * An equation can have multiple formulas on each side, 
 * e.g. X3 + Y2Z2 = ZX + Y2X2 + Z. 
 *
 * @author Lyndon While
 * @version 2021
 */
import java.util.ArrayList;

public class Equation
{
    // the two sides of the equation 
    // there can be multiple formulas on each side 
    private ArrayList<Formula> lhs, rhs;

    /**
     * Parses s to construct an equation. s will contain a 
     * syntactically legal equation, e.g. X3 + Y2Z = ZX + Y2X4. 
     * s may contain whitespace between formulas and symbols. 
     */
    public Equation(String s)
    {
        String[] equation = s.split("=");
        lhs = parseSide(equation[0]);
        rhs = parseSide(equation[1]);
    }

    /**
     * Returns the left-hand side of the equation.
     */
    public ArrayList<Formula> getLHS()
    {
        return lhs;
    }

    /**
     * Returns the right-hand side of the equation.
     */
    public ArrayList<Formula> getRHS()
    {
        return rhs;
    }
    
    /**
     * Returns the indices at which x occurs in s, 
     * e.g. indicesOf("ax34x", 'x') returns <1,4>. 
     */
    public static ArrayList<Integer> indicesOf(String s, char x)
    {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == x) indices.add(i);}
        return indices;
    }
    
    /**
     * Parses s as one side of an equation. 
     * s will contain a series of formulas separated by pluses, 
     * and it may contain whitespace between formulas and symbols. 
     */
    public static ArrayList<Formula> parseSide(String s)
    {
        //Extension is added
        ArrayList<Formula> forms = new ArrayList<>();
        String[] formula_arry = s.split("\\+");
        for (String str: formula_arry){
            str = str.strip();
            if (Character.isDigit(str.charAt(0))){
            forms.add(new Formula(coefficient(str)));}
            else forms.add(new Formula(str));
        }
        return forms;
    }

    /**
     * This if method which helps extension part in Project 1
     * This will take coefficient and effect to all related constant for chemicals.
     */
    public static String coefficient(String st)
    {
        int coe = findCoefficient(st);
        int new_count = 1;
        String new_string = "";
        ArrayList<String> elems = new ArrayList<>();
        for (int i = st.length()-1; i > 0; i--){
            if (Character.isUpperCase(st.charAt(i))){
                elems.add(st.substring(i));
                st = st.substring(0,i);
            }
        } 
        for (String str: elems){
            if (str.length() > 1){
                new_count = Integer.parseInt(str.substring(1)) * coe;}
            else new_count *= coe;
            new_string = new_string + str.substring(0,1) + new_count;
        }  
        return new_string;
    }

    
    /**
     * This if method which helps extension part in Project 1
     * This will find coefficient in string.
     */
    public static Integer findCoefficient(String st)
    {
        for (int i = 0; i < st.length(); i++){
            if (Character.isUpperCase(st.charAt(i))){
                return Integer.parseInt(st.substring(0,i));}
            }
        return 1;
    }
    
    /**
     * Returns true iff the equation is balanced, i.e. it has the 
     * same number of atoms of each Bydysawd element on each side. 
     */
    public boolean isValid()
    {
        String lhs_string = "";
        for (Formula f: lhs){lhs_string += f.display();}
        Formula f1 = new Formula(lhs_string);
        
        String rhs_string = "";
        for (Formula f: rhs){rhs_string += f.display();}
        Formula f2 = new Formula(rhs_string);
        
        return f1.isIsomer(f2);
    }

    /**
     * Returns the equation as a String.
     */
    public String display()
    {
        String eqn = "";
        for (Formula f: lhs){
            eqn = eqn + f.display() + "+";}
        eqn = eqn.substring(0,eqn.lastIndexOf("+"));
        eqn += "=";
        for (Formula f: rhs){
            eqn = eqn + f.display() + "+";}
        eqn = eqn.substring(0,eqn.lastIndexOf("+"));
        return eqn;
    }
}
