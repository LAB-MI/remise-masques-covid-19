package fr.gouv.interieur.dmgp.remettant;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "fr.gouv.interieur.dmgp.remettant")
public class LayeredArchitectureTest {

    private static final String ROOT_PATH = "fr.gouv.interieur.dmgp.remettant";

    private static final String APPLICATION_NAME = "application";
    private static final String APPLICATION_PATH = ROOT_PATH + ".application..";

    private static final String DOMAIN_NAME = "domain";
    private static final String DOMAIN_PATH = ROOT_PATH + ".domain..";

    private static final String INFRASTRUCTURE_NAME = "infrastructure";
    private static final String INFRASTRUCTURE_PATH = ROOT_PATH + ".infrastructure..";

    @ArchTest
    static final ArchRule layer_architecture_dependencies_sont_bien_respectees =
            layeredArchitecture()
                    .layer(APPLICATION_NAME).definedBy(APPLICATION_PATH)
                    .layer(DOMAIN_NAME).definedBy(DOMAIN_PATH)
                    .layer(INFRASTRUCTURE_NAME).definedBy(INFRASTRUCTURE_PATH)
                    .whereLayer(APPLICATION_NAME).mayNotBeAccessedByAnyLayer()
                    .whereLayer(DOMAIN_NAME).mayOnlyBeAccessedByLayers(APPLICATION_NAME, INFRASTRUCTURE_NAME)
                    .whereLayer(INFRASTRUCTURE_NAME).mayNotBeAccessedByAnyLayer();
}
