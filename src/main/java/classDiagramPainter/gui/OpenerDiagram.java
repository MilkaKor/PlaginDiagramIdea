package classDiagramPainter.gui;

import classDiagramPainter.gui.utils.DiagramLightFile;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;

public class OpenerDiagram {
    public static FileEditor[] openDiagramWindow(Project project, VirtualFile virtualFile, boolean b) {
        DiagramLightFile lightFile = new DiagramLightFile(virtualFile.getName());
        lightFile.setOriginalFile(virtualFile);

        if (EditorDiagram.getInitActionCreate().equals(virtualFile.getUserData(EditorDiagram.getInitAction()))) {
            FileEditor[] openedEditors = FileEditorManager.getInstance(project).getEditors(lightFile);
            EditorDiagram editor = null;
            for (FileEditor fileEditor : openedEditors) {
                if (fileEditor instanceof EditorDiagram) {
                    editor = (EditorDiagram) fileEditor;
                    try {
                        editor.createGraph();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        FileEditor[] a = FileEditorManager.getInstance(project).openFile(lightFile, b);
        lightFile.getOriginalFile().putUserData(EditorDiagram.getInitAction(), "");
        return a;
    }
}
