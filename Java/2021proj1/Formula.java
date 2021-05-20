/**
 * Formula represents a Bydysawd chemical formula. 
 * A formula is a sequence of terms, e.g. AX3YM67. 
 *
 * @author Lyndon While
 * @version 2021
 */
import java.util.ArrayList;
import java.util.Collections;

public class Formula
{
    // the constituent terms of the formula
    private ArrayList<Term> terms;
    
    /**
     * Makes a formula containing a copy of terms.
     */
    public Formula(ArrayList<Term> terms)
    {
        this.terms = (ArrayList<Term>) terms.clone();
    }

    /**
     * Parses s to construct a formula. s will be a legal sequence 
     * of terms with no whitespace, e.g. "AX3YM67" or "Z".  
     * The terms in the field must be in the same order as in s. 
     */
    public Formula(String s)
    {
        terms = new ArrayList<>();
        while (lastUC(s) != -1){
            terms.add(new Term(s.substring(lastUC(s))));
            s = s.substring(0,lastUC(s));
        }
        Collections.reverse(terms);
    }
    /**
     * Returns the terms of the formula.
     */
    public ArrayList<Term> getTerms()
    {
        return terms;
    }
    
    /**
     * Returns the index in s where the rightmost upper-case letter sits, 
     * e.g. lastTerm("AX3YM67") returns 4. 
     * Returns -1 if there are no upper-case letters. 
     */
    public static int lastUC(String s)
    {
        for (int i = s.length()-1; i >= 0; i--){
            if (Character.isUpperCase(s.charAt(i))) return i;
        }
        return -1;
    }
    
    /**
     * Returns the total number of atoms of element in terms. 
     * e.g. if terms = <<W,2>,<X,1>,<W,5>>, countElement('W') returns 7, 
     * countElement('X') returns 1, and countElement('Q') returns 0.
     */
    public int countElement(char element)
    {
        int total = 0;
        for (Term t: terms){
            if(t.getElement() == element) total += t.getCount();
        }
        return total;
    }

    /**
     * Puts terms in standardised form, where each element present is 
     * represented by exactly one term, and terms are in alphabetical order.
     * e.g. <<C,3>,<D,1>,<B,2>,<D,2>,<C,1>> becomes <<B,2>,<C,4>,<D,3>>.
     */
    public void standardise()
    {
        ArrayList<Character> chars = new ArrayList<>(); //filter distinct key.
        ArrayList<Term> new_terms = new ArrayList<>(); //for sorting purpose.
        for (Term t: terms){
            if (!chars.contains(t.getElement()))chars.add(t.getElement());
        }
        Collections.sort(chars);
        for (Character c: chars){
            new_terms.add(new Term(c,countElement(c)));
        }
        terms.clear();
        terms.addAll(new_terms);
    }
    
    /**
     * Returns true iff this formula and other are isomers, 
     * i.e. they contain the same number of every Bydysawd element. 
     */
    public boolean isIsomer(Formula other)
    {
        standardise();
        String test1 = display();
        other.standardise();
        String test2 = other.display();
        return test1.equals(test2) ;
    }

    /**
     * Returns the formula as a String. 
     * e.g. if terms = <<B,22>,<E,1>,<D,3>>, it returns "B22ED3". 
     */
    public String display()
    {
        String name = "";
        for (Term t: terms){
            if (t.getCount() == 1) name += t.getElement();
            else name = name + t.getElement() + t.getCount();
        }
        return name;
    }
}
