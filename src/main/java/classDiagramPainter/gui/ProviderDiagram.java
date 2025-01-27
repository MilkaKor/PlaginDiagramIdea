package classDiagramPainter.gui;

import classDiagramPainter.gui.utils.DiagramLightFile;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class ProviderDiagram implements FileEditorProvider, DumbAware {

    private static String EDITOR_TYPE_ID = "DiagramView";

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile lightFile) {
        boolean accept = false;
        if (!(lightFile instanceof LightVirtualFile)) {
            return accept;
        }
        VirtualFile virtualFile = ((LightVirtualFile) lightFile).getOriginalFile();
        if (virtualFile != null && project != null && virtualFile.isDirectory()) {
            PsiDirectory dir = PsiManager.getInstance(project).findDirectory(virtualFile);
            PsiPackage pkg = JavaDirectoryService.getInstance().getPackage(dir);
            if (pkg != null) {
                accept = true;
            }
        }
        return accept;
    }

    @Override
    public @NotNull
    FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile lightFile) {
        VirtualFile virtualFile = ((DiagramLightFile) lightFile).getOriginalFile();
        FileEditor[] openedEditors = FileEditorManager.getInstance(project).getEditors(lightFile);
        EditorDiagram editor = null;
        for (FileEditor fileEditor : openedEditors) {
            if (fileEditor instanceof EditorDiagram) {
                editor = (EditorDiagram) fileEditor;
                break;
            }
        }
        try {
            if (EditorDiagram.getInitActionCreate().equals(virtualFile.getUserData(EditorDiagram.getInitAction()))) {
                if (editor == null) {
                    editor = new EditorDiagram(lightFile, project);
                }
                lightFile.refresh(false, true);
                editor.createGraph();
            } else {
                if (editor == null && EditorDiagram.openable(virtualFile.getPath())) {
                    editor = new EditorDiagram(lightFile, project);
                    editor.openGraph();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return editor;
    }

    @Override
    public @NotNull
    @NonNls
    String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    @Override
    public @NotNull
    FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }

    public static ProviderDiagram getInstance() {
        return Objects.requireNonNull(FileEditorProvider.EP_FILE_EDITOR_PROVIDER.findFirstAssignableExtension(ProviderDiagram.class));
    }
}
