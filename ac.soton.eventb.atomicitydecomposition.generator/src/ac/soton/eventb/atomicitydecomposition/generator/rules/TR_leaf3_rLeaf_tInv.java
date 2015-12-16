package ac.soton.eventb.atomicitydecomposition.generator.rules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eventb.emf.core.EventBElement;
import org.eventb.emf.core.machine.Machine;

import ac.soton.eventb.atomicitydecomposition.All;
import ac.soton.eventb.atomicitydecomposition.FlowDiagram;
import ac.soton.eventb.atomicitydecomposition.Leaf;
import ac.soton.eventb.atomicitydecomposition.One;
import ac.soton.eventb.atomicitydecomposition.Par;
import ac.soton.eventb.atomicitydecomposition.Some;
import ac.soton.eventb.atomicitydecomposition.generator.strings.Strings;
import ac.soton.eventb.atomicitydecomposition.generator.utils.Utils;
import ac.soton.eventb.emf.diagrams.generator.AbstractRule;
import ac.soton.eventb.emf.diagrams.generator.GenerationDescriptor;
import ac.soton.eventb.emf.diagrams.generator.IRule;
import ac.soton.eventb.emf.diagrams.generator.utils.Make;

public class TR_leaf3_rLeaf_tInv extends AbstractRule implements IRule {
	
	
	@Override
	public boolean enabled(EventBElement sourceElement) throws Exception  {
		Leaf sourceLeaf = (Leaf) sourceElement;
		
		return 	(sourceLeaf.getDecompose().isEmpty()) && (
				(sourceLeaf.eContainer() instanceof All) ||
				(sourceLeaf.eContainer() instanceof Some) ||
				(sourceLeaf.eContainer() instanceof One) ||
				(sourceLeaf.eContainer() instanceof Par));
		
		/*//Dana: fixed to add type during refinement
		 //Do it later will need to add a loop to the tranlsation or may be add a general method for this because used more than once
		return sourceLeaf.getDecompose().isEmpty() &&
				!Utils.repAncestor(sourceLeaf).isEmpty();*/
	}

	
	/**
	 * TR_leaf3, Transform a replicator leaf to a typing invariant
	 */
	@Override
	public List<GenerationDescriptor> fire(EventBElement sourceElement, List<GenerationDescriptor> generatedElements) throws Exception {
		List<GenerationDescriptor> ret = new ArrayList<GenerationDescriptor>();
		Leaf sourceLeaf = (Leaf) sourceElement;
		Machine	container = (Machine)EcoreUtil.getRootContainer(sourceElement);	
		
		String name = Strings.INV_ + sourceLeaf.getName() + Strings._TYPE;
		
		String predicate = generatePredicate(sourceLeaf);
		
		ret.add(Make.descriptor(container, invariants, Make.invariant(name, predicate, ""), 1));	
	
		return ret;
		
		
	}


	private String generatePredicate(Leaf l) {
		String pred = "";
		FlowDiagram parentFLow = Utils.getParentFlow(l);
		// SI case
		if(parentFLow.getParameters().isEmpty()){
			if(l.eContainer() instanceof All)
				pred =  l.getName() + Strings.B_SUBSETEQ + ((All)l.eContainer()).getNewParameter().getType();
			else if(l.eContainer() instanceof Some)
				pred = l.getName() + Strings.B_SUBSETEQ + ((Some)l.eContainer()).getNewParameter().getType();
			else if(l.eContainer() instanceof One)
				pred = l.getName() + Strings.B_SUBSETEQ + ((One)l.eContainer()).getNewParameter().getType();
			else if(l.eContainer() instanceof Par)
				pred = l.getName() + Strings.B_SUBSETEQ + ((Par)l.eContainer()).getNewParameter().getType();
		}
		
		//Dana fixed the MI case
		else{
				if(l.eContainer() instanceof All){
					pred = l.getName() + Strings.B_SUBSETEQ + Utils.getParTypeCartesian(parentFLow.getParameters()) +
							Strings.B_CPROD + ((All)l.eContainer()).getNewParameter().getType();
				}
				else if(l.eContainer() instanceof Some){
					pred = l.getName() + Strings.B_SUBSETEQ + Utils.getParTypeCartesian(parentFLow.getParameters()) +
							Strings.B_CPROD + ((Some)l.eContainer()).getNewParameter().getType();
				}
				else if(l.eContainer() instanceof One){
					pred = l.getName() + Strings.B_SUBSETEQ + Utils.getParTypeCartesian(parentFLow.getParameters()) +
							Strings.B_CPROD + ((One)l.eContainer()).getNewParameter().getType();
				}
				else if(l.eContainer() instanceof Par){
					pred = l.getName() + Strings.B_SUBSETEQ + Utils.getParTypeCartesian(parentFLow.getParameters()) +
							Strings.B_CPROD + ((Par)l.eContainer()).getNewParameter().getType();
				}
				
			
		}
		return pred;
	}
	

}
