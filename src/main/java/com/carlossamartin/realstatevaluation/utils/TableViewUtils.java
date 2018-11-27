/*
 * Real State Valuation Project
 *
 * Copyright © 2018 Carlos Sánchez Martín (carlos.samartin@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.carlossamartin.realstatevaluation.utils;

import com.carlossamartin.realstatevaluation.model.HomeTable;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class TableViewUtils {

    private static final String TAB = "\t";
    private static final String LINE_FEED = "\n";

    public static void copySelectedToClipBoard(TableView<HomeTable> tableView)
    {
        copySelectedToClipBoard(tableView,false);
    }

    public static void copySelectedToClipBoard(TableView<HomeTable> tableView, boolean addTitle)
    {
        ObservableList<TablePosition> posList = tableView.getSelectionModel().getSelectedCells();
        int old_r = -1;

        StringBuilder clipboardString = new StringBuilder();
        if(addTitle)
        {
            clipboardString.append(getColumnsNames(tableView));
        }

        for (TablePosition p : posList) {
            int r = p.getRow();
            int c = p.getColumn();
            Object cell = tableView.getVisibleLeafColumns().get(c).getCellData(r);

            if (cell == null){
                cell = "";
            }

            //If the cell is in the equals row than before add TAB
            if (old_r == r) {
                clipboardString.append(TAB);
            }
            else if (old_r != -1) {
                clipboardString.append(LINE_FEED);
            }

            // Replace . by , to export to spreadsheet
            if(cell instanceof Integer || cell instanceof Double || cell instanceof Float) {
                clipboardString.append(cell.toString().replace('.',','));
            }else {
                clipboardString.append(cell.toString());
            }

            old_r = r;
        }

        final ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);
    }

    private static String getColumnsNames(TableView<HomeTable> tableView)
    {
        ObservableList<TableColumn<HomeTable, ?>> columns = tableView.getVisibleLeafColumns();

        StringBuilder columnsText = new StringBuilder();
        int size = columns.size();
        for (int i=0; i<size; i++){
            columnsText.append(columns.get(i).getText());

            if(i < size-1) {
                columnsText.append(TAB);
            }
            else {
                columnsText.append(LINE_FEED);
            }
        }

        return columnsText.toString();
    }
}
