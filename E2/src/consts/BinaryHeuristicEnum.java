package consts;

public enum BinaryHeuristicEnum implements HeuristicEnum {
    BH_IN_ORDER,
    BH_IN_ORDER_AND_DOMAIN_ORDER,
    BH_MOST_IN_ROWS_COLS,
    BH_MOST_IN_ROWS_COLS_AND_DOMAIN_ORDER,
    BH_SMALLEST_DOMAIN,
    BH_SMALLEST_DOMAIN_AND_DOMAIN_ORDER
}
