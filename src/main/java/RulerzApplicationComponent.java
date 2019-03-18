import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RulerzApplicationComponent implements ApplicationComponent, EditorFactoryListener {
    protected HashMap<Editor, RulerzCaretListener> _listeners = null;

    @Override
    public void initComponent() {
        _listeners = new HashMap<Editor, RulerzCaretListener>();
        EditorFactory.getInstance().addEditorFactoryListener(this);
    }

    @Override
    public void disposeComponent() {
        //Remove listener for editors
        EditorFactory.getInstance().removeEditorFactoryListener(this);
        for (RulerzCaretListener value : _listeners.values())
            value.dispose();
        _listeners.clear();
    }

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        if(editor.getProject() == null)
          return;
        RulerzCaretListener listener = new RulerzCaretListener(this,event.getEditor());
        _listeners.put(event.getEditor(),listener);
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
        RulerzCaretListener listener = _listeners.remove(event.getEditor());
        if(listener == null)
          return;
        listener.dispose();
    }
}
