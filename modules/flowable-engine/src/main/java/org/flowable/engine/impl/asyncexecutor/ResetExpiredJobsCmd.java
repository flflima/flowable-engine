/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.engine.impl.asyncexecutor;

import java.util.Collection;

import org.flowable.engine.impl.interceptor.Command;
import org.flowable.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.JobInfoEntity;
import org.flowable.engine.impl.persistence.entity.JobInfoEntityManager;
import org.flowable.engine.runtime.Job;

/**
 * @author Joram Barrez
 */
public class ResetExpiredJobsCmd implements Command<Void> {

    protected Collection<String> jobIds;
    protected JobInfoEntityManager<? extends JobInfoEntity> jobEntityManager;
    
    public ResetExpiredJobsCmd(Collection<String> jobsIds, JobInfoEntityManager<? extends JobInfoEntity> jobEntityManager) {
        this.jobIds = jobsIds;
        this.jobEntityManager = jobEntityManager;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        for (String jobId : jobIds) {
            Job job = commandContext.getJobEntityManager().findById(jobId);
            commandContext.getJobManager().unacquire(job);
            jobEntityManager.resetExpiredJob(jobId);
        }
        return null;
    }

}
