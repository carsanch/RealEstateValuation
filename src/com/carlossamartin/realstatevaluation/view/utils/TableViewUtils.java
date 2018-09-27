package com.carlossamartin.realstatevaluation.view.utils;

import com.carlossamartin.realstatevaluation.model.idealista.HomeTable;
import javafx.collections.ObservableList;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class TableViewUtils {

    public static final String TAB = "\t";
    public static final String LINE_FEED = "\n";

    public static void copyToClipBoard(TableView<HomeTable> tableView)
    {
        ObservableList<TablePosition> posList = tableView.getSelectionModel().getSelectedCells();
        int old_r = -1;
        StringBuilder clipboardString = new StringBuilder();
        for (TablePosition p : posList) {
            int r = p.getRow();
            int c = p.getColumn();
            Object cell = tableView.getVisibleLeafColumns().get(c).getCellData(r);
            if (cell == null)
                cell = "";
            if (old_r == r)
                clipboardString.append(TAB);
            else if (old_r != -1)
                clipboardString.append(LINE_FEED);
            clipboardString.append(cell);
            old_r = r;
        }
        final ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);
    }
}
