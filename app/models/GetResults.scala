package models

case class GetResults (
                        ok: Boolean,
                        result: Seq[BaseGetUpdatesModel]
                      )
//This is a base model for get data
