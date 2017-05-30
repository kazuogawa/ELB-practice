package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class Board(
  id: Long,
  title: Option[String] = None,
  contents: Option[String] = None,
  createdAt: DateTime) {

  def save()(implicit session: DBSession): Board = Board.save(this)(session)

  def destroy()(implicit session: DBSession): Int = Board.destroy(this)(session)

}


object Board extends SQLSyntaxSupport[Board] {

  override val tableName = "board"

  override val columns = Seq("id", "title", "contents", "created_at")

  def apply(b: SyntaxProvider[Board])(rs: WrappedResultSet): Board = apply(b.resultName)(rs)
  def apply(b: ResultName[Board])(rs: WrappedResultSet): Board = new Board(
    id = rs.get(b.id),
    title = rs.get(b.title),
    contents = rs.get(b.contents),
    createdAt = rs.get(b.createdAt)
  )

  val b = Board.syntax("b")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession): Option[Board] = {
    withSQL {
      select.from(Board as b).where.eq(b.id, id)
    }.map(Board(b.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession): List[Board] = {
    withSQL(select.from(Board as b)).map(Board(b.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    withSQL(select(sqls.count).from(Board as b)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[Board] = {
    withSQL {
      select.from(Board as b).where.append(where)
    }.map(Board(b.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[Board] = {
    withSQL {
      select.from(Board as b).where.append(where)
    }.map(Board(b.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    withSQL {
      select(sqls.count).from(Board as b).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: Option[String] = None,
    contents: Option[String] = None,
    createdAt: DateTime)(implicit session: DBSession): Board = {
    val generatedKey = withSQL {
      insert.into(Board).namedValues(
        column.title -> title,
        column.contents -> contents,
        column.createdAt -> createdAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Board(
      id = generatedKey,
      title = title,
      contents = contents,
      createdAt = createdAt)
  }

  def batchInsert(entities: Seq[Board])(implicit session: DBSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'title -> entity.title,
        'contents -> entity.contents,
        'createdAt -> entity.createdAt))
    SQL("""insert into board(
      title,
      contents,
      created_at
    ) values (
      {title},
      {contents},
      {createdAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Board)(implicit session: DBSession): Board = {
    withSQL {
      update(Board).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.contents -> entity.contents,
        column.createdAt -> entity.createdAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Board)(implicit session: DBSession): Int = {
    withSQL { delete.from(Board).where.eq(column.id, entity.id) }.update.apply()
  }

}
