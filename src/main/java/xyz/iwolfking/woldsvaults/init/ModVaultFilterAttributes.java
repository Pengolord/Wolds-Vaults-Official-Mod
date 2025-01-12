package xyz.iwolfking.woldsvaults.init;

import xyz.iwolfking.woldsvaults.integration.vaultfilters.*;

public class ModVaultFilterAttributes {
    public static void initAttributes() {
        new HasUnusualAffixAttribute(true).register(HasUnusualAffixAttribute::new);
        new UnusualPrefixAttribute("Attack Damage").register(UnusualPrefixAttribute::new);
        new UnusualSuffixAttribute("Attack Damage").register(UnusualSuffixAttribute::new);
        new OfferingItemAttribute("").register(OfferingItemAttribute::new);
        new OfferingModifierAttribute("").register(OfferingModifierAttribute::new);
        new IsRottenOfferingAttribute(true).register(IsRottenOfferingAttribute::new);
        new EtchedLayoutTypeAttribute("Infinite").register(EtchedLayoutTypeAttribute::new);
        new EtchedLayoutTunnelAttribute(1).register(EtchedLayoutTunnelAttribute::new);
        new EtchedLayoutValueAttribute(1).register(EtchedLayoutValueAttribute::new);
        new AntiqueAttribute("Acquired taste").register(AntiqueAttribute::new);
    }
}
