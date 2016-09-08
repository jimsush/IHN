package dima.config.common.controls;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumberDocument extends PlainDocument {

    private static final long serialVersionUID = 7805513298683713919L;

    private int maxLength=8;
    public NumberDocument(int maxLength){
    	this.maxLength=maxLength;
    }
    
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
    	if(str==null)
    		return;
    	
    	if(getLength()+str.length()>maxLength){
            return; 
        } 
    	
    	String oldStr = getText(0, getLength());

        boolean hasSign = oldStr.length() > 0;

        char[] source = str.toCharArray();
        char[] result = new char[source.length];
        int i = 0;

        for (char c : source) {
            if (!hasSign && source[0] == '-' || Character.isDigit(c)) {
                hasSign = true;
                result[i++] = c;
            } else {
                break;
            }
        }

        super.insertString(offs, new String(result, 0, i), a);
    }
    
}
