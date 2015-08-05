package coolor.dto;

/**
 * Created by KAKolesnikov on 2015-08-05.
 */
public class CurrencyDTO {
    private float currency;
    private float cost;

    public CurrencyDTO() {
    }

    public CurrencyDTO(float currency, float cost) {
        this.currency = currency;
        this.cost = cost;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getCurrency() {
        return currency;
    }

    public void setCurrency(float currency) {
        this.currency = currency;
    }
}
