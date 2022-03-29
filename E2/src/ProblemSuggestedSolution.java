public interface ProblemSuggestedSolution <D, V> {
    void setValue(D domainItem, V variableItem);
    boolean isCurrentCorrect();
    boolean isCompleted();
    boolean wereNegativelyAffected();
}
