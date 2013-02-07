package org.deri.wsmo4j.orchestration;

import org.wsmo.common.*;
import org.wsmo.service.orchestration.*;

import com.ontotext.wsmo4j.mediator.*;

public class PpMediatorRi extends MediatorImpl implements PpMediator {

    public PpMediatorRi(Identifier id) {
        super((IRI) id);
    }
}
