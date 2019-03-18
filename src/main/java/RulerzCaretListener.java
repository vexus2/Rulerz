import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.util.TextRange;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RulerzCaretListener implements CaretListener {
    protected RulerzApplicationComponent _appComponent = null;
    protected Editor _editor = null;
    private static String pattern = "([^\\x01-\\x7E])";
    private static Pattern regexPattern = Pattern.compile(pattern);

    public RulerzCaretListener(RulerzApplicationComponent appComponent, Editor editor) {
        _appComponent = appComponent;
        _editor = editor;
        _editor.getCaretModel().addCaretListener(this);
    }


    public void dispose() {
        _editor.getCaretModel().removeCaretListener(this);
        _editor = null;
    }

    @Override
    public void caretPositionChanged(CaretEvent e) {
        Editor editor = e.getEditor();
        String currentLineText = e.getEditor().getDocument().getText(new TextRange(e.getCaret().getVisualLineStart(), e.getCaret().getSelectionEnd()));
        Matcher matcher = regexPattern.matcher(currentLineText);
        int multiByteTextCount = 0;
        while (matcher.find()) multiByteTextCount++;
        if (multiByteTextCount > 1) {
            multiByteTextCount = (multiByteTextCount / 2);
        }
        editor.getSettings().setSoftMargins(Arrays.asList(e.getCaret().getVisualPosition().column + multiByteTextCount));
    }

    @Override
    public void caretAdded(CaretEvent e) {
        // ignore the event
    }

    @Override
    public void caretRemoved(CaretEvent e) {
        // ignore the event
    }
}

