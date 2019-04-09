package com.eshel.jump;

import com.eshel.jump.anno.AContext;
import com.eshel.jump.anno.Action;
import com.eshel.jump.anno.Category;
import com.eshel.jump.anno.Data;
import com.eshel.jump.anno.ExtraParams;
import com.eshel.jump.anno.Flag;
import com.eshel.jump.anno.Intent;
import com.eshel.jump.anno.IntentParser;
import com.eshel.jump.anno.Params;
import com.eshel.jump.anno.TargetClass;
import com.eshel.jump.anno.TargetName;
import com.eshel.jump.anno.Type;

import java.lang.annotation.Annotation;

/**
 * createBy Eshel
 * createTime: 2019/3/30 14:29
 * desc: TODO
 */
public class AnnoProvider {

	Annotation[] annos;
	Annotation anno;
	public AnnoProvider() {}

	private void closeParser(){
		anno = null;
		annos = null;
	}
	public void initParser(Annotation[] annos){
		closeParser();
		this.annos = annos;
	}

	public void initParser(Annotation anno){
		closeParser();
		this.anno = anno;
	}

	public AContext getContextAnno(){
		return (AContext) getAnno(AContext.class);
	}

	public Action getActionAnno(){
		return (Action) getAnno(Action.class);
	}

	public Category getCategoryAnno(){
		return (Category) getAnno(Category.class);
	}

	public ExtraParams getExtraParamsAnno(){
		return (ExtraParams) getAnno(ExtraParams.class);
	}

	public Flag getFlagAnno(){
		return (Flag) getAnno(Flag.class);
	}

	public Intent getIntentAnno(){
		return (Intent) getAnno(Intent.class);
	}

	public IntentParser getIntentParserAnno(){
		return (IntentParser) getAnno(IntentParser.class);
	}

	public Params getParamsAnno(){
		return (Params) getAnno(Params.class);
	}

	public TargetClass getTargetClassAnno(){
		return (TargetClass) getAnno(TargetClass.class);
	}

	public TargetName getTargetNameAnno(){
		return (TargetName) getAnno(TargetName.class);
	}

	public Type getTypeAnno(){
		return (Type) getAnno(Type.class);
	}

	public Data getDataAnno() {
		return (Data) getAnno(Data.class);
	}

	private Annotation getAnno(Class<? extends Annotation> annoClass){
		if(annoClass.isInstance(anno))
			return anno;
		if(annos != null){
			for (Annotation annotation : annos) {
				if(annoClass.isInstance(annotation))
					return annotation;
			}
		}
		return null;
	}

}
