package modules;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import modules.financialmodule.model.FinancialEntry;

public abstract class Module {

    protected TableColumn addColumn(String name) {
        TableColumn<FinancialEntry, ?> column = new TableColumn<>(name);
        column.setMinWidth(200);
        column.setCellValueFactory(new PropertyValueFactory<>(name));
        return column;
    }

    protected Label getLabel(String name, String option) {
        Label label = new Label(name);
        switch (option) {
            case "BOLD" : label.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                break;
            default:
        }
        return getLabel(label);
    }

    protected Label getLabel(String name) {
        Label label = new Label(name);
        return getLabel(label);
    }

    protected Label getLabel(Label label) {
        label.setPadding(new Insets(5,5,5,5));
        label.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return label;
    }

    protected TextField getTextField(String name) {
        TextField textField = new TextField();
        textField.setPromptText(name);
        textField.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return textField;
    }
}
