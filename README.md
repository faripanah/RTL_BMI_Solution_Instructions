# Quick steps — toggle JavaFX UI between LTR and RTL

  - Short, copy-pasteable instructions to make your JavaFX UI mirror (RTL) or stay normal (LTR).

## 1. Structure your FXML rows with HBox
  - VBox alone won’t mirror horizontal order. Put each label+field pair into an HBox:

```xml
<VBox fx:id="rootVBox" ...>
  <HBox spacing="10">
    <Label fx:id="lblWeight" minWidth="100" />
    <TextField fx:id="tfWeight" />
  </HBox>

  <HBox spacing="10">
    <Label fx:id="lblHeight" minWidth="100" />
    <TextField fx:id="tfHeight" />
  </HBox>

  <Button fx:id="btnCalculate" onAction="#onCalculateClick" />
  ...
</VBox>

```
## 2. Give the root container an fx:id and inject it in the controller
  - FXML top element must be addressable:

```java
@FXML private VBox rootVBox;
@FXML private TextField tfWeight, tfHeight;
@FXML private Label lblWeight, lblHeight, lblResult, lblLocalTime;

```

## 3. Add a method to apply direction (use Platform.runLater)
- Set NodeOrientation on the root container to flip the whole layout. Use Platform.runLater to ensure the scene is ready:

```java

import javafx.application.Platform;
import javafx.geometry.NodeOrientation;

private void applyTextDirection(Locale locale) {
    boolean isRTL = List.of("fa","ur","ar","he").contains(locale.getLanguage());

    Platform.runLater(() -> {
        if (rootVBox != null) {
            rootVBox.setNodeOrientation(isRTL ? NodeOrientation.RIGHT_TO_LEFT
                                             : NodeOrientation.LEFT_TO_RIGHT);
        }

        // Make text inside TextFields flow from right for RTL
        tfWeight.setNodeOrientation(isRTL ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
        tfHeight.setNodeOrientation(isRTL ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
    });
}

```
## 4. Call applyTextDirection(...) after loading locale strings
- When you change language (ResourceBundle), call the method so layout and text both update:

```java
private void setLanguage(Locale locale) {
    rb = ResourceBundle.getBundle("messages", locale);
    lblWeight.setText(rb.getString("weight"));
    lblHeight.setText(rb.getString("height"));
    btnCalculate.setText(rb.getString("calculate"));

    displayLocalTime(locale);
    applyTextDirection(locale);
}

```
## 5. If you want only text alignment (keep component order LTR)
- Do not change root node orientation. Instead set orientation or styles on individual controls:
```java
// keep layout LTR but right-align text inside controls
tfWeight.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
lblWeight.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

```

## 6. Ensure resource bundles and UTF-8 files exist
- If switching languages, have messages.properties and messages_fa.properties (saved as UTF-8) so labels update when locale changes.

## 7. Quick checklist / troubleshooting

  - Did you wrap label+field in HBox? ✅

  - Is rootVBox injected (fx:id + @FXML)? ✅

  - Are you calling applyTextDirection() after the scene is shown? Use Platform.runLater. ✅

  - Are your resource bundles present and in resources/? ✅
