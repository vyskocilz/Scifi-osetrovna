package validators;

import org.jdesktop.beansbinding.Validator;

import javax.swing.*;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class PositiveValidator extends Validator<Number> {
    @Override
    public Result validate(Number value) {
        if (value == null
                || (value instanceof Integer && (Integer) value < 0)
                || (value instanceof Long && (Long) value < 0L)) {
            JOptionPane.showMessageDialog(null, "Číslo musí být nezáporný");
            return new Result(null, null);
        }
        return null;
    }
}
