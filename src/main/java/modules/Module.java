package modules;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import modules.financialmodule.model.FinancialEntry;

public abstract class Module {

    protected TableColumn addColumn(String name) {
        TableColumn<FinancialEntry, ?> column = new TableColumn<>(name);
        column.setMinWidth(200);
        column.setCellValueFactory(new PropertyValueFactory<>(name));
        return column;
    }
}
