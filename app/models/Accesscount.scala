package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class Accesscount(
  id: Long,
  createdAt: DateTime) {

  def save()(implicit session: DBSession): Accesscount = Accesscount.save(this)(session)

  def destroy()(implicit session: DBSession): Int = Accesscount.destroy(this)(session)

}


object Accesscount extends SQLSyntaxSupport[Accesscount] {

  override val tableName = "accesscount"

  override val columns = Seq("id", "created_at")

  def apply(a: SyntaxProvider[Accesscount])(rs: WrappedResultSet): Accesscount = apply(a.resultName)(rs)
  def apply(a: ResultName[Accesscount])(rs: WrappedResultSet): Accesscount = new Accesscount(
    id = rs.get(a.id),
    createdAt = rs.get(a.createdAt)
  )

  val a = Accesscount.syntax("a")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession): Option[Accesscount] = {
    withSQL {
      select.from(Accesscount as a).where.eq(a.id, id)
    }.map(Accesscount(a.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession): List[Accesscount] = {
    withSQL(select.from(Accesscount as a)).map(Accesscount(a.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    withSQL(select(sqls.count).from(Accesscount as a)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[Accesscount] = {
    withSQL {
      select.from(Accesscount as a).where.append(where)
    }.map(Accesscount(a.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[Accesscount] = {
    withSQL {
      select.from(Accesscount as a).where.append(where)
    }.map(Accesscount(a.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    withSQL {
      select(sqls.count).from(Accesscount as a).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    createdAt: DateTime)(implicit session: DBSession): Accesscount = {
    val generatedKey = withSQL {
      insert.into(Accesscount).namedValues(
        column.createdAt -> createdAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Accesscount(
      id = generatedKey,
      createdAt = createdAt)
  }

  def batchInsert(entities: Seq[Accesscount])(implicit session: DBSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'createdAt -> entity.createdAt))
    SQL("""insert into accesscount(
      created_at
    ) values (
      {createdAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Accesscount)(implicit session: DBSession): Accesscount = {
    withSQL {
      update(Accesscount).set(
        column.id -> entity.id,
        column.createdAt -> entity.createdAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Accesscount)(implicit session: DBSession): Int = {
    withSQL { delete.from(Accesscount).where.eq(column.id, entity.id) }.update.apply()
  }

}
