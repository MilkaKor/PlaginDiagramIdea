package classDiagramPainter.actions;

import classDiagramPainter.gui.EditorDiagram;
import classDiagramPainter.gui.OpenerDiagram;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class DiagramCreate extends ADiagram {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        VirtualFile virtualFile = anActionEvent.getData(PlatformDataKeys.VIRTUAL_FILE);
        virtualFile.putUserData(EditorDiagram.getInitAction(), EditorDiagram.getInitActionCreate());
        OpenerDiagram.openDiagramWindow(project, virtualFile, true);
    }
}
