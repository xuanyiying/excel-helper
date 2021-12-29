import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

config = [
        impSerializable  : true,
        extendBaseEntity : true,
        extendBaseService: true

]
baseEntityPackage = "com.yija.project.framework.base.BaseEntity"
baseServicePackage = "com.yija.project.framework.base.BaseService"
baseEntityProperties = ["id", "createDate", "lastModifiedDate", "version"]
typeMapping = [
        (~/(?i)bool|boolean|tinyint/)     : "Boolean",
        (~/(?i)bigint/)                   : "Long",
        (~/int/)                          : "Integer",
        (~/(?i)float|double|decimal|real/): "Double",
        (~/(?i)datetime|timestamp/)       : "java.util.Date",
        (~/(?i)date/)                     : "java.sql.Date",
        (~/(?i)time/)                     : "java.sql.Time",
        (~/(?i)/)                         : "String"
]

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter {
        it instanceof DasTable && it.getKind() == ObjectKind.TABLE
    }.each {
        generate(it, dir)
    }
}
//dif -> E:\DEVELOPE\Code\jpademo\src\main\java\com\demo\jpa
def generate(table, dir) {

    def entityPath = "${dir.toString()}\\entity",
        servicePath = "${dir.toString()}\\service",
        repPath = "${dir.toString()}\\repository",
        repImpPath = "${dir.toString()}\\repository\\impl"

    mkdirs([entityPath, servicePath, repPath, repImpPath])

    def entityName = javaName(table.getName(), true)
    def fields = calcFields(table)
    def basePackage = clacBasePackage(dir)

    new File("${entityPath}\\${entityName}.java").withPrintWriter { out -> genEntity(out, table, entityName, fields, basePackage) }
    new File("${servicePath}\\${entityName}Service.java").withPrintWriter { out -> genService(out, table, entityName, fields, basePackage) }
    new File("${repPath}\\${entityName}Repository.java").withPrintWriter { out -> genRepository(out, table, entityName, fields, basePackage) }
    new File("${repPath}\\${entityName}RepositoryCustom.java").withPrintWriter { out -> genRepositoryCustom(out, entityName, basePackage) }
    new File("${repImpPath}\\${entityName}RepositoryImpl.java").withPrintWriter { out -> genRepositoryImpl(out, table, entityName, fields, basePackage) }

}

def genProperty(out, field) {
    if (field.annos != "") out.println "  ${field.annos}"
    if (field.colum != field.name) {
        out.println "\t@Column(name = \"${field.colum}\")"
    }
    out.println "\tprivate ${field.type} ${field.name};"
    out.println ""
}

def genEntity(out, table, entityName, fields, basePackage) {
    out.println "package ${basePackage}.entity;"
    out.println ""
    if (config.extendBaseEntity) {
        out.println "import $baseEntityPackage;"
    }
    out.println "import lombok.Data;"
    out.println ""
    if (config.impSerializable) {
        out.println "import java.io.Serializable;"
        out.println ""
    }
    out.println "import javax.persistence.*;"
    out.println ""
    out.println "@Data"
    out.println "@Entity"
    out.println "@Table(name = \"${table.getName()}\")"
    out.println "public class $entityName${config.extendBaseEntity ? " extends BaseEntity" : ""}${config.impSerializable ? " implements Serializable" : ""} {"
    out.println ""
    if (config.extendBaseEntity) {
        // 继承父类
        fields.each() { if (!isBaseEntityProperty(it.name)) genProperty(out, it) }
    } else {
        // 不继承父类
        out.println "\t@Id"
        fields.each() { genProperty(out, it) }
    }
    out.println "}"

}

def genService(out, table, entityName, fields, basePackage) {
    out.println "package ${basePackage}.service;"
    out.println ""
    out.println "import ${basePackage}.repository.${entityName}Repository;"
    if (config.extendBaseService) {
        out.println "import $baseServicePackage;"
        out.println "import ${basePackage}.entity.$entityName;"
    }
    out.println "import org.springframework.stereotype.Service;"
    out.println ""
    out.println "import javax.annotation.Resource;"
    out.println ""
    out.println "@Service"
    out.println "public class ${entityName}Service${config.extendBaseService ? " extends BaseService<$entityName, ${fields[0].type}>" : ""}  {"
    out.println ""
    out.println "\t@Resource"
    out.println "\tprivate ${entityName}Repository rep;"
    out.println "}"
}

def genRepository(out, table, entityName, fields, basePackage) {
    out.println "package ${basePackage}.repository;"
    out.println ""
    out.println "import ${basePackage}.entity.$entityName;"
    out.println "import org.springframework.data.jpa.repository.JpaRepository;"
    out.println ""
    out.println "public interface ${entityName}Repository extends JpaRepository<$entityName, ${fields[0].type}>, ${entityName}RepositoryCustom{}"
}

def genRepositoryCustom(out, entityName, basePackage) {
    out.println "package ${basePackage}.repository;"
    out.println ""
    out.println "public interface ${entityName}RepositoryCustom { "
    out.println "}"
}

def genRepositoryImpl(out, table, entityName, fields, basePackage) {
    out.println "package ${basePackage}.repository.impl;"
    out.println ""
    out.println "import ${basePackage}.repository.${entityName}RepositoryCustom;"
    out.println "import org.springframework.stereotype.Repository;"
    out.println ""
    out.println "import javax.persistence.EntityManager;"
    out.println "import javax.persistence.PersistenceContext;"
    out.println ""
    out.println "@Repository"
    out.println "public class ${entityName}RepositoryImpl implements ${entityName}RepositoryCustom {"
    out.println ""
    out.println "\t@PersistenceContext"
    out.println "\tprivate EntityManager em;"
    out.println "}"
}

def mkdirs(dirs) {
    dirs.forEach {
        def f = new File(it)
        if (!f.exists()) {
            f.mkdirs();
        }
    }
}

def clacBasePackage(dir) {
    dir.toString()
            .replaceAll("^.+\\\\src\\\\main\\\\java\\\\", "")
            .replaceAll("\\\\", ".")
}

def isBaseEntityProperty(property) {
    baseEntityProperties.find { it == property } != null
}
// 转换类型
def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) {
        fields, col ->
            def spec = Case.LOWER.apply(col.getDataType().getSpecification())
            def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
            fields += [[
                               name : javaName(col.getName(), false),
                               colum: col.getName(),
                               type : typeStr,
                               annos: ""]]
    }
}

def javaName(str, capitalize) {
    def s = str.split(/(?<=[^\p{IsLetter}])/).collect { Case.LOWER.apply(it).capitalize() }
            .join("").replaceAll(/[^\p{javaJavaIdentifierPart}]/, "_").replaceAll(/_/, "")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}
