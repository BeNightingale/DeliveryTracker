package track.app.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PolishPostRequest {

    String language;
    String number;
    boolean addPostOfficeInfo;
}
