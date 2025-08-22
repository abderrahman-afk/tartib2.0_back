package com.solidwall.tartib.enums;


public enum ProjectStaut {
    brouillon("Fiche-projet brouillon"),
    finalisée("Fiche-projet finalisée"),
    validé("Fiche-projet validée"),
    eligible("Éligible"),
    non_eligible("Inéligible"),
    en_cours_auto_evaluation("En cours d'autoévaluation"),
    auto_évalué("Autoévaluation finalisée"),
    auto_évaluation_validée("Autoévaluation validée"),

    bloqué("Fiche-projet bloqué"),
    en_cours_admin_evaluation("En cours d'évaluation"),
    en_phase_contradictoire("en_phase_contradictoire"),

    évalué("Évalué"),
    archivé("Archivé"),
    validé_par_cnap("Validé par CNAP"),
    validé_avec_reserve("Validé avec réserve"),
    rejeté_par_cnap ("Rejeté par CNAP")
    ;
  

    
    private final String displayName;
    
    ProjectStaut(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
}