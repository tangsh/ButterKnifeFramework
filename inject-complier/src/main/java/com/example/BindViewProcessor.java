package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by TSH on 2017/3/23.
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

	/**
	 * 注解处理器 去处理哪些注解
	 * 注解 BindView.class
	 *
	 * @return
	 */

	public Set<String> getSupportedAnnotationTypes() {
		Set<String> sets = new HashSet<>();
		sets.add(BindView.class.getCanonicalName());
//		return super.getSupportedAnnotationTypes();
		return sets;
	}


	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	/**
	 * 处理节点的工具类
	 */
	private Elements elementUtils;

	private Types typesUtils;

	// 生成java文件辅助类
	private Filer filer;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnvironment) {
		super.init(processingEnvironment);
		elementUtils = processingEnvironment.getElementUtils();
		typesUtils = processingEnvironment.getTypeUtils();
		filer = processingEnvironment.getFiler();
	}

	public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

		// 存放差app所有的注解类型的成员变量 TypeElement. --> MainActivity
		Map<TypeElement, List<FieldViewBinding>> targetMap = new HashMap<>();

		for (Element element : roundEnvironment.getElementsAnnotatedWith(BindView.class)) {
			FileUtils.print(element.getSimpleName().toString());
			TypeElement enClosingElement = (TypeElement) element.getEnclosingElement();
			List<FieldViewBinding> list = targetMap.get(enClosingElement);
			if (list == null) {
				list = new ArrayList<>();
				targetMap.put(enClosingElement, list);
			}
			//
			int id = element.getAnnotation(BindView.class).value();
			String fieldName = element.getSimpleName().toString();
			TypeMirror typeMirror = element.asType();
			FileUtils.print(typeMirror.toString());
			//
			FieldViewBinding fieldViewBinding = new FieldViewBinding(fieldName, typeMirror, id);
			list.add(fieldViewBinding);
		}

		for (Map.Entry<TypeElement, List<FieldViewBinding>> item : targetMap.entrySet()) {
			List<FieldViewBinding> list = item.getValue();
			if (list.isEmpty()) {
				continue;
			}

			TypeElement enClosingElement = item.getKey();

			String packageName = getPackageName(enClosingElement);
			String complite = getClassName(enClosingElement, packageName);
			ClassName className = ClassName.bestGuess(complite);
			ClassName viewBinder = ClassName.get("com.tsh.inject", "ViewBinder");
			//
			TypeSpec.Builder classBuilder =
					TypeSpec.classBuilder(complite + "$$ViewBinder")
							.addModifiers(Modifier.PUBLIC)
							.addTypeVariable(TypeVariableName.get("T", className))
							.addSuperinterface(ParameterizedTypeName.get(viewBinder, className));
			//
			MethodSpec.Builder methodBuilder =
					MethodSpec.methodBuilder("bind")
							.addModifiers(Modifier.PUBLIC)
							.returns(TypeName.VOID)
							.addAnnotation(Override.class)
							.addParameter(className, "target", Modifier.FINAL);

			//
			for (int i = 0; i < list.size(); i++) {

				FieldViewBinding fieldViewBinding = list.get(i);
				String packageNameString = fieldViewBinding.getTypeMirror().toString();
				ClassName viewClass = ClassName.bestGuess(packageNameString);
				methodBuilder.addStatement("target.$L=($T)target.findViewById($L)",
						fieldViewBinding.getName(), viewClass, fieldViewBinding.getResId());
			}

			classBuilder.addMethod(methodBuilder.build());

			try {
				JavaFile.builder(packageName, classBuilder.build())
						.addFileComment("auto generated class ")
						.build().writeTo(filer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	private String getClassName(TypeElement enClosingElement, String packageName) {
		int packageLength = packageName.length() + 1;
		return enClosingElement.getQualifiedName().toString().substring(packageLength).replace(".", "$");
	}

	private String getPackageName(TypeElement enClosingElement) {
		return elementUtils.getPackageOf(enClosingElement).getQualifiedName().toString();

	}
}
